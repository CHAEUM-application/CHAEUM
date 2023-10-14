package com.example.life

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextID: EditText
    private lateinit var editTextPW: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextID = findViewById(R.id.edit_id)
        editTextPW = findViewById(R.id.edit_pw)

        val loginButton: Button = findViewById(R.id.btn_login)
        val joinButton: Button = findViewById(R.id.btn_join)

        editTextPW.setOnEditorActionListener{ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                loginButton.performClick()
                handled = true
            }
            handled
        }

        loginButton.setOnClickListener {
            val id = editTextID.text.toString().trim()
            val pw = editTextPW.text.toString().trim()

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "ID와 PW를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // API를 호출해 DB에서 id와 pw 확인
                RetrofitClient.api.getUserID(id).enqueue(object : Callback<UsersDTO> {
                    override fun onResponse(call: Call<UsersDTO>, response: Response<UsersDTO>) {
                            val result = response.body()
                            if (result != null && id == result.c_id && pw == result.c_pw) {
                                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply{
                                    putExtra("id", result.c_id)
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                // 로그인 실패
                                showLoginError()
                            }
                        }
                    override fun onFailure(call: Call<UsersDTO>, t: Throwable) {
                        showLoginError()
                    }
                })
            }
        }
        joinButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoginError() {
        Toast.makeText(this, "ID와 PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@LoginActivity)
        builder.setMessage("애플리케이션을 종료하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    finishAffinity()
                    System.runFinalization()
                    System.exit(0)
                })
            .setNegativeButton("취소",null)
        // 다이얼로그를 띄워주기
        builder.show()
    }
}
