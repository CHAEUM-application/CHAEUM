package com.example.life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)

        val nameText = findViewById<EditText>(R.id.nameText)
        val joinButton = findViewById<Button>(R.id.joinButton)
        val datePicker = findViewById<DatePicker>(R.id.datepicker)

        joinButton.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("name", nameText.text.toString())
                putExtra("year", year)
                putExtra("month", month)
                putExtra("dayOfMonth", dayOfMonth)
            }
            startActivity(intent)
        }
    }
}
