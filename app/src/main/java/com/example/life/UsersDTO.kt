package com.example.life

import com.google.gson.annotations.*
import java.sql.Date

data class UsersDTO(
    @SerializedName("c_name") val c_name: String,
    @SerializedName("c_id") val c_id: String,
    @SerializedName("c_pw") val c_pw: String,
    @SerializedName("c_date") @JsonAdapter(DateDeserializer::class) val c_date: Date
)
