package com.example.life

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.Year

class YearActivity : AppCompatActivity() {

    private lateinit var adapter: MonthAdapter  // Add this line
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
        yearTextView.text = selectedYear.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = id?.let { MonthAdapter(selectedYear, it) }

        adapter = MonthAdapter(selectedYear, id ?: "")  // Modify this line
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
