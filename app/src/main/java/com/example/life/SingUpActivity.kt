package com.example.life

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar
import java.sql.Date

class SingUpActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
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
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val date = Date(calendar.timeInMillis)

            if (name.isEmpty() || id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "정보를 다 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                RetrofitClient.api.postUsersInfo(name, id, pw, date).enqueue(object: Callback<UsersDTO> {
                    override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                        val result = response.body()
                        if (result != null) {
                            Toast.makeText(this@SingUpActivity, "성공", Toast.LENGTH_SHORT).show()
                    }else {
                            Toast.makeText(this@SingUpActivity, "실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                        Toast.makeText(this@SingUpActivity, "오류", Toast.LENGTH_SHORT).show()
                    }
                })

                //val intent = Intent(this, MainActivity::class.java).apply {
                //    putExtra("name", name)
                //    putExtra("year", year)
                //    putExtra("month", month)
                //    putExtra("dayOfMonth", dayOfMonth)
                }
                //startActivity(intent)
            }
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
