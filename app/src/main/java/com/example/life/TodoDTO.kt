package com.example.life

import com.google.gson.annotations.SerializedName

data class TodoDTO(
    @SerializedName("id") val id: String,
    @SerializedName("year") val year: String,
    @SerializedName("month") val month: String,
    @SerializedName("week") val week: String,
    @SerializedName("text") val text: String,
    @SerializedName("status") val status: Int
)
