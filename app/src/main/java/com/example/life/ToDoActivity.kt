package com.example.life

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        adapter = ToDoAdapter(mutableListOf(), progressBar)
        recyclerView.adapter = adapter

        val addButton = findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            adapter.addTodo(
                ToDo(
                    adapter.itemCount + 1,
                    "",
                    false
                )
            )
            if (id != null) {
                RetrofitClient.api.insTodoInfo(id, year, month, week, "", 0).enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        val result = response.body()
                        return
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
                adapter.removeTodo(adapter.itemCount - 1)
                progressBar.max = adapter.getTodosCount()
            }
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
