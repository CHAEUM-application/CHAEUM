package com.example.life

import YearAdapter
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lifepage) // Assume your layout is activity_main.xml

        val birthYear = intent.getIntExtra("year", 0)
        val name = intent.getStringExtra("name")

        val userBirthday = findViewById<TextView>(R.id.userBirthday)
        val userName = findViewById<TextView>(R.id.userName)

        userName.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        userBirthday.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)

        userName.text = name
        userBirthday.text = "${birthYear} ~ ${birthYear + 90}"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 10) // Assume 10 items per row
        recyclerView.adapter = YearAdapter(birthYear)

        val homeButton = findViewById<Button>(R.id.homeBtn)
        homeButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}


