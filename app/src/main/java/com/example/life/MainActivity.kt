package com.example.life

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Year

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
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

                    updateUI(name, birthYear, id)
                }

                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                    updateUI(name, birthYear, id)
                }
            })
        } else {
                return
                //updateUI(name, birthYear, id)
        }

        val enterButton = findViewById<Button>(R.id.enterBtn)
        enterButton.setOnClickListener {
            val intent = Intent(this, YearActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("year", Year.now().value)
            startActivity(intent)
        }

        val homeButton = findViewById<Button>(R.id.homeBtn)
        homeButton.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun updateUI(name: String?, birthYear: Int, id: String) {
        val userBirthday = findViewById<TextView>(R.id.userBirthday)
        val userName = findViewById<TextView>(R.id.userName)

        userName.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        userBirthday.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)

        userName.text = name
        userBirthday.text = "${birthYear} ~ ${birthYear + 90}"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 10) // Assume 10 items per row
        recyclerView.adapter = MainAdapter(birthYear, id, context = this) // Pass context here
    }
}

