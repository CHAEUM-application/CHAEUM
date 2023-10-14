package com.example.life

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.sql.Date
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private var validatePW = false  //비밀번호 형식통과했는지
    private var validateID= false  //아이디 중복검사했는지


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val idText = findViewById<EditText>(R.id.edit_joinID)
        val pwText = findViewById<EditText>(R.id.edit_joinPW)
        val doublePW= findViewById<EditText>(R.id.edit_checkPW)
        val checkPW = findViewById<TextView>(R.id.checkIDPW)
        val nameText = findViewById<EditText>(R.id.edit_name)

        val checkIdButton= findViewById<Button>(R.id.btn_CheckID)
        val joinButton = findViewById<Button>(R.id.btn_join)
        val backButton = findViewById<ImageButton>(R.id.backBtn)
        val datePicker = findViewById<DatePicker>(R.id.datepicker)

        checkIdButton.setOnClickListener( View.OnClickListener
        //ID중복검사 버튼
        {
            val id: String = idText.text.toString().trim()
            if (validateID) {
                return@OnClickListener  //검증 완료
            }
            if (id == "") { //아이디 입력안했을 때
                Toast.makeText(this@SignUpActivity, "아이디를 입력하세요", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            //DB에  가입된 아이디인지 체크하는 함수
            RetrofitClient.api.getUserID(id).enqueue(object : Callback<UsersDTO> {
                override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                    if(response.isSuccessful){
                        Toast.makeText(this@SignUpActivity, "이미 가입된 아이디입니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                    // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)

                    //Log.d(TAG, "테이블에 존재하지 않는 ID라 등록가능" + t.message.toString())
                    if (t.message == "End of input at line 1 column 1 path $") {
                        Toast.makeText(this@SignUpActivity, "사용가능한 아이디입니다", Toast.LENGTH_SHORT).show()
                        idText.setEnabled(false) //아이디값 고정
                        validateID = true //검증 완료
                    }
                }
            })
        })


        pwText.addTextChangedListener(object : TextWatcher {
            //비밀번호에 리스너를 달아서 비밀번호 형식에 맞는지 검사 부분
            //형식은 그냥 영문,숫자 포함 4자리이상 10자리 이하로 지정하였습니다.
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length >=4) {
                    val p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{4,10}$")
                    val m = p.matcher(s.toString())
                    if (!m.matches()) {
                        //0xAARRGGBB
                        checkPW.setTextColor(-0x5510b7b6)  //red
                        checkPW.setText("영문,숫자 포함 10자이내")
                        validatePW = false
                    } else {
                        checkPW.setTextColor(Color.parseColor("#377E47"))  //green
                        checkPW.setText("사용 가능한 비밀번호입니다.")
                        validatePW = true
                    }
                } else  //6글자 미만
                {
                    checkPW.setTextColor(-0x5510b7b6)
                    checkPW.setText("영문,숫자 포함 4자리 이상")
                    validatePW = false
                }
            }
        })

        joinButton.setOnClickListener {
            val name = nameText.text.toString().trim()
            val id = idText.text.toString().trim()
            val pw = pwText.text.toString().trim()
            val dPW= doublePW.text.toString().trim()
            val year = datePicker.year
            val month = datePicker.month
            val dayOfMonth = datePicker.dayOfMonth
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val date = Date(calendar.timeInMillis)

            if (name.isEmpty() || id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (!validateID) {
                Toast.makeText(this@SignUpActivity, "중복확인을 진행해주세요", Toast.LENGTH_SHORT).show()
            } else if (!validatePW) {
                Toast.makeText(this@SignUpActivity, "유효한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else if (pw != dPW) {
                Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }else{

                RetrofitClient.api.postUsersInfo(name, id, pw, date).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        val result = response.body()
                        Log.d("SignUpActivity", "API Response: $response")
                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java).apply {
                            putExtra("id", id)
                        }
                        startActivity(intent)
                        finish()

                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "오류", Toast.LENGTH_SHORT).show()
                        Log.e("SignUpActivity", "API Failure: ${t.message}", t)
                    }
                })

            }


/*
            {
                RetrofitClient.api.getUserID(id).enqueue(object : Callback<UsersDTO> {
                    override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                        Toast.makeText(this@SignUpActivity, "중복된 ID 입니다.", Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                        RetrofitClient.api.postUsersInfo(name, id, pw, date).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    val result = response.body()
                                    Log.d("SignUpActivity", "API Response: $response")
                                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@SignUpActivity, MainActivity::class.java).apply {
                                            putExtra("id", id)
                                        }
                                        startActivity(intent)
                                        finish()

                                }
                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@SignUpActivity, "오류", Toast.LENGTH_SHORT).show()
                                    Log.e("SignUpActivity", "API Failure: ${t.message}", t)
                                }
                            })
                    }
                })
            }

 */
        }
        backButton.setOnClickListener {
            onBackPressed()
        }

    }


}
