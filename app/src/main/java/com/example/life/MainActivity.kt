package com.example.life

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

    lateinit var name: String
    lateinit var birthDay: String



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.lifepage)
//
//        var birthYear = intent.getIntExtra("year", 0)
//        var name = intent.getStringExtra("name")
//        val id = intent.getStringExtra("id")
//
//        if (id != null) {
//            RetrofitClient.api.getUserID(id).enqueue(object: Callback<UsersDTO>{
//                override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
//                    val result = response.body()
//                    name = result?.c_name ?: name
//                    val calendar = Calendar.getInstance()
//                    calendar.time = result?.c_date
//                    birthYear = calendar.get(Calendar.YEAR)
//
//                    updateUI(name, birthYear, id)
//                }
//
//                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
//                    updateUI(name, birthYear, id)
//                }
//            })
//        } else {
//                return
//            if (id != null) {
//                updateUI(name, birthYear, id)
//            }
//        }
//
//        val enterButton = findViewById<Button>(R.id.enterBtn)
//        enterButton.setOnClickListener {
//            val intent = Intent(this, YearActivity::class.java)
//            intent.putExtra("id", id)
//            intent.putExtra("year", Year.now().value)
//            startActivity(intent)
//        }
//
//        val homeButton = findViewById<Button>(R.id.homeBtn)
//        homeButton.setOnClickListener {
//            val intent = Intent(this, StartActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setContentView(R.layout.lifepage)

        var birthYear = intent.getIntExtra("year", 0)
        var name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")

        if (id != null) {
            RetrofitClient.api.getUserID(id).enqueue(object: Callback<UsersDTO>{
                override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                    val result = response.body()
                    name = result?.c_name.toString()
                    birthDay = result?.c_date.toString()
                    //val calendar = Calendar.getInstance()
                    //calendar.time = result?.c_date
                    //val birthYear = calendar.get(Calendar.YEAR)
                    Log.d("TAG",result?.c_date.toString())
                    updateUI(id)
                }

                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                    //updateUI(name, birthDay, id)
                }
            })
        } else {
            return
            if (id != null) {
                updateUI(id)
            }
        }

        val enterButton = findViewById<ImageView>(R.id.enterBtn)
        enterButton.setOnClickListener {
            val intent = Intent(this, YearActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("year", Year.now().value)
            startActivity(intent)
        }

        val homeButton = findViewById<ImageButton>(R.id.homeBtn)
        homeButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUI(id:String) {
        val userBirthday = findViewById<TextView>(R.id.userBirthday)
        val userName = findViewById<TextView>(R.id.userName)
        val userAge = findViewById<TextView>(R.id.userAge)
        val thisYear= findViewById<TextView>(R.id.thisYear)

        userName.text = name
        birthDay= birthDay.replace("-",".").toString()
        userBirthday.text = birthDay+" ~"

        val year= birthDay?.substring(0,4)?.toInt()!!
        thisYear.text=Year.now().value.toString()
        userAge.text= "나이: "+ (Year.now().value -year +1).toString() +"살"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 10) // Assume 10 items per row
        recyclerView.adapter = MainAdapter(year, id, context = this) // Pass context here
    }
}

