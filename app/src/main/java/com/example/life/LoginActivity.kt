package com.example.life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextID: EditText
    private lateinit var editTextPW: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)

        editTextID = findViewById(R.id.editTextID)
        editTextPW = findViewById(R.id.editTextPW)

        val loginButton: Button = findViewById(R.id.loginButton)
        val backButton: Button = findViewById(R.id.backButton)

        loginButton.setOnClickListener {
            val id = editTextID.text.toString().trim()
            val pw = editTextPW.text.toString().trim()

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "ID와 PW를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 로그인 성공 시 MainActivity로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 현재 액티비티를 종료하여 뒤로가기 시 돌아올 수 없도록 함
            }
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
