package com.example.life

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ToDoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        // retrieve the selectedWeek from the intent
        val selectedWeek = intent.getIntExtra("selectedWeek", -1)

        // Do something with selectedWeek...
    }
}