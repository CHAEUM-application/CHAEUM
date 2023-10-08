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
    ): Call<List<ToDoDTO>>

    @FormUrlEncoded
    @POST("todo/{id}")
    fun insTodoInfo(
        @Path("id") id: String,
        @Field("year") year: String,
        @Field("month") month: String,
        @Field("week") week: String,
        @Field("text") text: String,
        @Field("status") status: Int,
        @Field("feel") feel: Int
    ): Call<Unit>

    @FormUrlEncoded
    @PUT("todo/{id}")
    fun updTodoInfo(
        @Path("id") id: String,
        @Field("year") year: String,
        @Field("month") month: String,
        @Field("week") week: String,
        @Field("req_text") req_text: String,
        @Field("res_text") res_text: String,
        @Field("status") status: Int,
        @Field("feel") feel: Int
    ): Call<Unit>

    //@FormUrlEncoded
    @HTTP(method = "DELETE", path= "/todo/{id}/{year}/{month}/{week}/{text}", hasBody = false)
    fun delTodoInfo(
        @Path("id") id: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("week") week: String,
        @Path("text") text: String
    ): Call<Unit>

}
