package com.example.life

import com.google.gson.annotations.SerializedName

data class ToDoDTO(
    @SerializedName("id") val id: String,
    @SerializedName("year") val year: String,
    @SerializedName("month") val month: String,
    @SerializedName("week") val week: String,
    @SerializedName("text") var text: String,
    @SerializedName("status") var status: Int,
    @SerializedName("feel") var feel: Int
)
