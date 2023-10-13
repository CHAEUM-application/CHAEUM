package com.example.life


import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate


class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //시스템상 다크모드 끄는거

        progressBar = findViewById(R.id.progressBar)
        progressBar.max=100

        val currentProgress = 100

        ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
            .setDuration(2500)
            .start()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        },3000)

    }
}