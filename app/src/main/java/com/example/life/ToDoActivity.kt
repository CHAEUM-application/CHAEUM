package com.example.life

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ToDoActivity : AppCompatActivity() {
    private lateinit var adapter: ToDoAdapter
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val selectedYear = intent.getIntExtra("selectedYear", -1)
        val selectedMonth = intent.getStringExtra("selectedMonth")
        val selectedWeek = intent.getIntExtra("selectedWeek", -1)

        //retrofit 사용 변수
        val id = intent.getStringExtra("id")
        val year = selectedYear.toString()
        val month = selectedMonth.toString()
        val week = selectedWeek.toString()

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = "Selected Year: $selectedYear, Month: $selectedMonth, Week: $selectedWeek, id: $id"

        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        if (id != null) {
            RetrofitClient.api.getTodoID(id).enqueue(object: Callback<List<ToDoDTO>>{
                override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                    val result = response.body()
                    val todoList = mutableListOf<ToDo>()
                    if (result != null) {
                        for(i in result){
                            val r_year = i.year
                            val r_month = i.month
                            val r_week = i.week
                            var tf: Boolean = false
                            if(i.status == 0){
                                tf = false
                            }
                            else if(i.status == 1){
                                tf = true
                            }
                            if(r_year == year && r_month == month && r_week == week) {
                                val text = i.text
                                val todo = ToDo(0, text, tf)
                                todoList.add(todo)
                                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                                recyclerView.layoutManager = LinearLayoutManager(this@ToDoActivity)
                                progressBar = findViewById<ProgressBar>(R.id.progressBar)

                                adapter = ToDoAdapter(todoList, progressBar, id, year, month, week, i.status)
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                    Toast.makeText(this@ToDoActivity, "오류", Toast.LENGTH_SHORT).show()
                }
            })
        }


        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            if (id != null) {
                RetrofitClient.api.insTodoInfo(id, year, month, week, "", 0).enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        val result = response.body()
                        adapter.addTodo(
                            ToDo(
                                adapter.itemCount + 1,
                                "",
                                false
                            )
                        )
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        return
                    }

                })
            }
            progressBar.max = adapter.getTodosCount()
        }

        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            if (adapter.itemCount > 0) {
                val position = adapter.itemCount - 1
                val text = adapter.getItemText(position)
                if (id != null) {
                    RetrofitClient.api.delTodoInfo(id, year, month, week, text)
                        .enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                adapter.removeTodo(position)
                                progressBar.max = adapter.getTodosCount()
                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                return
                            }
                        })
                }
            }
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
