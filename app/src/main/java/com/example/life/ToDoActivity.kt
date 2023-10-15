package com.example.life

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ToDoActivity : AppCompatActivity() {

    private lateinit var adapter: ToDoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        val selectedYear = intent.getIntExtra("selectedYear", -1)
        val selectedMonthString = intent.getStringExtra("selectedMonth")
        val selectedWeek = intent.getIntExtra("selectedWeek", -1)

        // 월 이름을 숫자로 매핑
        val monthMap = mapOf(
            "January" to 1, "February" to 2, "March" to 3, "April" to 4,
            "May" to 5, "June" to 6, "July" to 7, "August" to 8,
            "September" to 9, "October" to 10, "November" to 11, "December" to 12
        )

        val selectedMonth = monthMap[selectedMonthString] ?: 0

        val id = intent.getStringExtra("id")
        val year = selectedYear.toString()
        val month = selectedMonthString.toString()
        val week = selectedWeek.toString()

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = "$selectedYear" + "년 $selectedMonth" + "월 $selectedWeek" + "주차"

        val feelView = findViewById<TextView>(R.id.feelView)

        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ToDoAdapter(mutableListOf(), progressBar, feelView, id ?: "", year, month, week, 0, 0,  this@ToDoActivity)
        recyclerView.adapter = adapter

        id?.let {
            getTodos(it, year, month, week)
        }

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            id?.let {
                addTodo(it, year, month, week)
            }
        }

        val removeButton = findViewById<Button>(R.id.removeButton)
        removeButton.setOnClickListener {
            if (adapter.itemCount > 0) {
                val position = adapter.itemCount - 1
                val text = adapter.getItemText(position)
                id?.let {
                    removeTodo(it, year, month, week, text, position)
                }
            }
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getTodos(id: String, year: String, month: String, week: String) {
        RetrofitClient.api.getTodoID(id).enqueue(object: Callback<List<ToDoDTO>> {
            override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                val result = response.body()
                val todoList = mutableListOf<ToDo>()
                result?.let { todoDtos ->
                    for(dto in todoDtos) {
                        if(dto.year == year && dto.month == month && dto.week == week) {
                            val todo = ToDo(0, dto.feel, dto.text, dto.status == 1)
                            todoList.add(todo)
                        }
                    }
                }
                adapter.todos.clear()
                adapter.todos.addAll(todoList)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                Toast.makeText(this@ToDoActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addTodo(id: String, year: String, month: String, week: String) {
        RetrofitClient.api.insTodoInfo(id, year, month, week, "", 0, 0).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                adapter.addTodo(
                    ToDo(
                        adapter.itemCount + 1,
                        // 감정의 default는 0으로 처리 나머지 감정을 1~5로 설정
                        0, 
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

    private fun removeTodo(id: String, year: String, month: String, week: String, text: String, position: Int) {
        RetrofitClient.api.delTodoInfo(id, year, month, week, text)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if(response.isSuccessful) {
                        adapter.removeTodo(position)
                    } else {
                        Log.d("ResponseError", "Status Code: ${response.code()}")
                        Log.d("ResponseError", "Response Body: ${response.errorBody()?.string()}")
                        Toast.makeText(this@ToDoActivity, "Failed to delete ToDo", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@ToDoActivity, "Failed to delete ToDo", Toast.LENGTH_SHORT).show()
                }
            })
    }

}
