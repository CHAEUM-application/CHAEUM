package com.example.life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SingUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singuppage)

        val nameText = findViewById<EditText>(R.id.nameText)
        val idText = findViewById<EditText>(R.id.idText)
        val pwText = findViewById<EditText>(R.id.pwText)
        val joinButton = findViewById<Button>(R.id.joinButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val datePicker = findViewById<DatePicker>(R.id.datepicker)

        joinButton.setOnClickListener {
            val name = nameText.text.toString().trim()
            val id = idText.text.toString().trim()
            val pw = pwText.text.toString().trim()

            if (name.isEmpty() || id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "정보를 다 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val year = datePicker.year
                val month = datePicker.month
                val dayOfMonth = datePicker.dayOfMonth

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("year", year)
                    putExtra("month", month)
                    putExtra("dayOfMonth", dayOfMonth)
                }
                startActivity(intent)
            }
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
