package com.example.life

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag
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

        if (id != null) {
            RetrofitClient.api.getUserID(id).enqueue(object: Callback<UsersDTO>{
                override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                    var age = 0
                    val result = response.body()
                    val birthDay = result?.c_date.toString()
                    val year = birthDay?.substring(0,4)?.toInt()!!
                    age = (Year.now().value -year +1)
                    Log.d("TAG",age.toString())

                    val yearDialog = yearPickerDialog(selectedYear,age){
                        if (it!=selectedYear){
                            Toast.makeText(this@YearActivity, "${it}년 변경완료", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@YearActivity, YearActivity::class.java).apply{
                                putExtra("id", id)
                                Log.d("TAG",it.toString())
                                putExtra("selectedYear", it)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    yearTextView.setOnClickListener{
                        yearDialog.show(supportFragmentManager, "yearPickerDialog")
                    }
                }
                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                }

            })
        }


        val yearTarget1 = findViewById<EditText>(R.id.yearTarget1)
        val yearTarget2 = findViewById<EditText>(R.id.yearTarget2)
        val yearTarget3 = findViewById<EditText>(R.id.yearTarget3)

        yearTarget1.setPadding(25, 0, 0, 0)
        yearTarget2.setPadding(25, 0, 0, 0)
        yearTarget3.setPadding(25, 0, 0, 0)
        val yearBtn1 = findViewById<ImageButton>(R.id.yearBtn1)
        val yearBtn2 = findViewById<ImageButton>(R.id.yearBtn2)
        val yearBtn3 = findViewById<ImageButton>(R.id.yearBtn3)
        val targetCheck = findViewById<TextView>(R.id.targetCheck)

        var target1Empty = false
        var target2Empty = false
        var target3Empty = false

        var missingTargets: MutableList<String>
        val existingTargets = mutableSetOf<String>()

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
                                        target1Empty = yearTarget1.text.toString().trim().isEmpty()
                                        if (dto.status == 1) yearBtn1.isSelected = true
                                    }
                                    "102" -> {
                                        yearTarget2.text = editableText
                                        existingTargets.add("102")
                                        target2Empty = yearTarget2.text.toString().trim().isEmpty()
                                        if (dto.status == 1) yearBtn2.isSelected = true
                                    }
                                    "103" -> {
                                        yearTarget3.text = editableText
                                        existingTargets.add("103")
                                        target3Empty = yearTarget3.text.toString().trim().isEmpty()
                                        if (dto.status == 1) yearBtn3.isSelected = true

                                    }
                                }
                            }
                        }
                    }
                    fun btnCheck() {
                        var btnCnt = 0
                        for (btn in listOf(yearBtn1, yearBtn2, yearBtn3)) {
                            if (btn.isSelected) {
                                btnCnt++
                            }
                        }
                        targetCheck.text = when {
                            target1Empty && target2Empty && target3Empty -> "목표를 입력해주세요!😊"
                            btnCnt == 0 -> "얼른 시작하셔야겠어요!😯"
                            btnCnt == 1 -> "남은 목표도 도전해봐요!🤗"
                            btnCnt == 2 -> "마지막 목표까지 하나 남았어요!😆"
                            btnCnt == 3 -> "${selectedYear}년 연간 목표를 다 달성했어요!🥳"
                            else -> "no"
                        }
                    }
                    btnCheck()
                    targetBtnStatus(yearBtn1, id, selectedYear.toString(), "101", yearTarget1, ::btnCheck)
                    targetBtnStatus(yearBtn2, id, selectedYear.toString(), "102", yearTarget2, ::btnCheck)
                    targetBtnStatus(yearBtn3, id, selectedYear.toString(), "103", yearTarget3, ::btnCheck)

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
                    var preText = ""
                    yearTarget1.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                        if (hasFocus) {
                            // 포커스가 얻어질 때
                            preText = yearTarget1.text.toString()
                            Log.d("TAG", "포커스 얻음: $preText")
                        } else {
                            // 포커스를 잃을 때
                            val curText = yearTarget1.text.toString()
                            Log.d("TAG", "포커스 잃음: $curText")
                            // 여기에서 필요한 작업 수행
                        }
                    }

                    val yearTargets = listOf(yearTarget1, yearTarget2, yearTarget3)
                    val preTexts = Array<String?>(3) { null }

                    yearTargets.forEachIndexed { index, yearTarget ->
                        yearTarget.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                            if (hasFocus) {
                                preTexts[index] = yearTarget.text.toString()
                                Log.d("TAG", "yearTarget${index + 1} 포커스 얻음: ${preTexts[index]}")
                            } else {
                                val curText = yearTarget.text.toString()
                                Log.d("TAG", "yearTarget${index + 1} 포커스 잃음: $curText")
                                Log.d("TAG", "인덱스 : $index")
                                // 필요한 작업 수행
                            }
                        }
                    }

                    yearTargets.forEachIndexed { index, yearTarget ->
                        yearTarget.setOnEditorActionListener { textView, action, event ->
                            var handled = false
                            if (action == EditorInfo.IME_ACTION_DONE) {
                                val curText = yearTarget.text.toString()
                                when(index){
                                        0 -> target1Empty= curText.trim().isEmpty()
                                        1 -> target2Empty= curText.trim().isEmpty()
                                        2 -> target3Empty= curText.trim().isEmpty()
                                }
                                preTexts[index]?.let {
                                    RetrofitClient.api.updTodoInfo(id, selectedYear.toString(), "${index + 101}", "0", curText,
                                        it, -1, 0)
                                        .enqueue(object : Callback<Unit> {
                                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                                Log.d("TAG", "yearTarget${index + 1} 변경 성공")
                                                Log.d("TAG", "$id , $selectedYear")
                                                Log.d("TAG", "이전 텍스트: ${preTexts[index]}")
                                                Log.d("TAG", "현재 텍스트: $curText")
                                            }

                                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                                // 실패 처리
                                            }
                                        })
                                }
                                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                                yearTarget.clearFocus()
                                btnCheck()
                                handled = true
                            }
                            handled
                        }
                    }
                }

                override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                    Log.e("ERROR", "response error")
                }
            })
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = id?.let { MonthListAdapter(selectedYear, it) }

        adapter = MonthListAdapter(selectedYear, id ?: "")  // Modify this line
        recyclerView.adapter = adapter

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        adapter.refreshData()
    }

    // 연간 목표 달성 버튼 변경
    fun targetBtnStatus(targetButton: ImageButton, id: String, year: String, month: String, yearTarget: EditText, btnCheck: () -> Unit) {
        targetButton.setOnClickListener(View.OnClickListener {
            if (yearTarget.text.trim().isEmpty()){
                return@OnClickListener
            }
            val isSelected = targetButton.isSelected
            val status = if (isSelected) 0 else 1
            updTgt(id, year, month, yearTarget.text.toString(), status)
            targetButton.isSelected = !isSelected
            btnCheck()
        })
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
