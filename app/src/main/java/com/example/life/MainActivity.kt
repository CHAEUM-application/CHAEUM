package com.example.life

import YearAdapter
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lifepage)

        var birthYear = intent.getIntExtra("year", 0)
        var name = intent.getStringExtra("name")

        val id = intent.getStringExtra("id")
        if (id != null) {
            RetrofitClient.api.getUserID(id).enqueue(object: Callback<UsersDTO>{
                override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                    val result = response.body()
                    name = result?.c_name ?: name
                    val calendar = Calendar.getInstance()
                    calendar.time = result?.c_date
                    birthYear = calendar.get(Calendar.YEAR)

                    updateUI(name, birthYear)
                }

                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                    updateUI(name, birthYear)
                }
            })
        } else {
            updateUI(name, birthYear)
        }

        val homeButton = findViewById<Button>(R.id.homeBtn)
        homeButton.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }
    private fun updateUI(name: String?, birthYear: Int) {
        val userBirthday = findViewById<TextView>(R.id.userBirthday)
        val userName = findViewById<TextView>(R.id.userName)

        userName.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        userBirthday.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)

        userName.text = name
        userBirthday.text = "${birthYear} ~ ${birthYear + 90}"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 10) // Assume 10 items per row
        recyclerView.adapter = YearAdapter(birthYear, context = this) // Pass context here
    }
}

