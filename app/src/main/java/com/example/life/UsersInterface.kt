package com.example.life

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.sql.Date

interface UsersInterface {

    @GET("/user/{c_id}")
    fun UsersInfo(
        @Query("c_name") c_name:String,
        @Query("c_id") c_id:String,
        @Query("c_pw") c_pw:String,
        @Query("c_date") c_date:Date
    ): Call<UsersDTO>
}