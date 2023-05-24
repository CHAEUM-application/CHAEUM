package com.example.life

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: RetroService

    private const val BASE_URL = "http://chaeum-env.eba-kkids7m7.ap-northeast-2.elasticbeanstalk.com/"
    init {
        val gson=GsonBuilder().setLenient().create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetroService::class.java)
    }
}