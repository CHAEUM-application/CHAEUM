package com.example.life

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.Year

class YearActivity : AppCompatActivity() {

    private lateinit var adapter: MonthListAdapter  // Add this line
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)
        val id = intent.getStringExtra("id")

        // retrieve the selectedYear from the intent
        val selectedYear = intent.getIntExtra("selectedYear", Year.now().value)

        // Find the TextView and set its text to the selected year
        val yearTextView = findViewById<TextView>(R.id.yearTextView)
        yearTextView.text = "$selectedYear" + "년"

        val yearTarget1 = findViewById<EditText>(R.id.yearTarget1)
        val yearTarget2 = findViewById<EditText>(R.id.yearTarget2)
        val yearTarget3 = findViewById<EditText>(R.id.yearTarget3)
        val yearBtn1 = findViewById<Button>(R.id.yearBtn1)
        val yearBtn2 = findViewById<Button>(R.id.yearBtn2)
        val yearBtn3 = findViewById<Button>(R.id.yearBtn3)

//        if (id != null) {
//            RetrofitClient.api.getTodoID(id).enqueue(object: Callback<List<ToDoDTO>>{
//                override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
//                    val result = response.body()
//                    val todoList = mutableListOf<ToDo>()
//                    result?.let { todoDtos ->
//                        for(dto in todoDtos) {
//                            if(dto.year == "100" && dto.month == "100" && dto.week == "100") {
//                                return
//                            }
//                        }
//                    }
//
//                }
//
//                override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
//                    Log.e("ERROR", "response error")
//                }
//            })
//        }


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = id?.let { MonthListAdapter(selectedYear, it) }

        adapter = MonthListAdapter(selectedYear, id ?: "")  // Modify this line
        recyclerView.adapter = adapter

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        adapter.refreshData()
    }
}
