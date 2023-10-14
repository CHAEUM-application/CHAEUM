package com.example.life

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val targetCheck = findViewById<TextView>(R.id.targetCheck)

        var target1Empty = false
        var target2Empty = false
        var target3Empty = false

        var btnCnt = 0
        for (btn in listOf(yearBtn1,yearBtn2,yearBtn3)){
            if (!btn.isSelected){
                btnCnt++
            }
        }

        var missingTargets: MutableList<String>
        val existingTargets = mutableSetOf<String>()
        var targetCnt: Int = 0

        if (id != null) {
            RetrofitClient.api.getTodoID(id).enqueue(object: Callback<List<ToDoDTO>> {
                override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                    val result = response.body()
                    val targetList = listOf<String>("","","")
                    // 연간 목표 1,2,3이 db안에 있는지 없는지 확인하는 리스트
                    result?.let { targetDtos ->
                        for(dto in targetDtos) {
                            val editableText = Editable.Factory.getInstance().newEditable(dto.text)
                            // 연간 목표여서 year만 비교, month로 인덱스 text, status로 버튼
                            if (dto.year == selectedYear.toString() ) {
                                when (dto.month) {
                                    "101" -> {
                                        yearTarget1.text = editableText
                                        existingTargets.add("101")
                                        target1Empty = yearTarget1.text.toString().isEmpty()
                                    }
                                    "102" -> {
                                        yearTarget2.text = editableText
                                        existingTargets.add("102")
                                        target2Empty = yearTarget2.text.toString().isEmpty()
                                    }
                                    "103" -> {
                                        yearTarget3.text = editableText
                                        existingTargets.add("103")
                                        target3Empty = yearTarget3.text.toString().isEmpty()
                                    }
                                }
                            }
                        }
                    }
                    targetCheck.text = when {
                        target1Empty && target2Empty && target3Empty -> "목표를 입력해주세요!"
                        btnCnt == 0 -> "얼른 시작하셔야겠어요!"
                        btnCnt == 1 -> "남은 목표도 도전해봐요!"
                        btnCnt == 2 -> "마지막 목표까지 하나 남았어요!"
                        btnCnt == 3 -> "${selectedYear}년 연간 목표를 다 달성했어요!"
                        else -> "에이 설마 이건가"
                    }
                    // 타겟이 설정되어 있지 않다면 db insert 실행
                    missingTargets = mutableListOf("101", "102", "103").filter { it !in existingTargets } as MutableList<String>
                    if (missingTargets.isNotEmpty()) {
                        id?.let {
                            for (month in missingTargets) {
                                RetrofitClient.api.insTodoInfo(id,selectedYear.toString(),month,
                                    "0","",0,0).enqueue(object :Callback<Unit>{
                                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                        Log.d("target","생성완료")
                                    }

                                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                                    }
                                })
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                    Log.e("ERROR", "response error")
                }
            })
        }

        targetCheck.text = when {
            target1Empty && target2Empty && target3Empty -> "목표를 입력해주세요!"
            btnCnt == 0 -> "얼른 시작하셔야겠어요!"
            btnCnt == 1 -> "남은 목표도 도전해봐요!"
            btnCnt == 2 -> "마지막 목표까지 하나 남았어요!"
            btnCnt == 3 -> "${selectedYear}년 연간 목표를 다 달성했어요!"
            else -> "에이 설마 이건가"
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = id?.let { MonthListAdapter(selectedYear, it) }

        adapter = MonthListAdapter(selectedYear, id ?: "")  // Modify this line
        recyclerView.adapter = adapter

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        adapter.refreshData()
    }

    fun updTgt(id: String, year: String, month: String, text: String, status: Int){
        RetrofitClient.api.updTodoInfo(id, year, month, "0", text, text, status, 0).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("TARGET", "complete")
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("TARGET", "error")
            }
        })
    }
}
