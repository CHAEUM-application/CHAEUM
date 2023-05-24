package com.example.life

import retrofit2.Call
import retrofit2.http.*
import java.sql.Date

interface UsersService {

    @GET("user/{c_id}")
    fun getUserID(
        @Path("c_id") c_id: String
    ): Call<UsersDTO>

    @FormUrlEncoded
    @POST("user/{c_id}")
    fun postUsersInfo(
        @Field("c_name") c_name: String,
        @Path("c_id") c_id: String,
        @Field("c_pw") c_pw: String,
        @Field("c_date") c_date: Date
    ): Call<UsersDTO>

//    @GET("user/{c_id}")
//    fun getLoginInfo(
//        @Path("c_id") c_id: String,
//        @Path("c_pw") c_pw: String
//    ): Call<UsersDTO>
}