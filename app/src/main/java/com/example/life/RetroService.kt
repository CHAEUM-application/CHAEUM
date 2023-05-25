package com.example.life

import retrofit2.Call
import retrofit2.http.*
import java.sql.Date

interface RetroService {

    //Users 테이블 부분
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
    ): Call<Void>

    
    //todo 테이블 부분
    @GET("todo/{id}")
    fun getTodoID(
        @Path("id") id: String
    ): Call<TodoDTO>

    @FormUrlEncoded
    @POST("todo/{id}")
    fun postTodoInfo(
        @Path("id") id: String,
        @Field("year") year: String,
        @Field("month") month: String,
        @Field("week") week: String,
        @Field("text") text: String,
        @Field("status") status: Int
    ): Call<Unit>

    @FormUrlEncoded
    @PUT("todo/{id}/{year}/{month}/{week}")
    fun putTodoInfo(
        @Path("id") id: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("week") week: String,
        @Field("text") text: String
    ): Call<Unit>

    @FormUrlEncoded
    @DELETE("/todo/{id}/{year}/{month}/{week}/{text}")
    fun delTodoInfo(
        @Path("id") id: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("week") week: String,
        @Path("text") text: String
    ): Call<Unit>

}
