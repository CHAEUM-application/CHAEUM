package com.example.life

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthAdapter(
    private val year: Int,
    private val age : Int,
    private val id: String,
    private val context: Context
) : RecyclerView.Adapter<MonthViewHolder>() {

    private val sadColorStart = Color.parseColor("#f5f3f2")  // 0%
    private val sadColorEnd = Color.parseColor("#a4928e")   // 100%
    private val sosoColorStart = Color.parseColor("#fdf7f1")  // 0%
    private val sosoColorEnd = Color.parseColor("#f0b57b")   // 100%
    private val goodColorStart = Color.parseColor("#f0f9f3")  // 0%
    private val goodColorEnd = Color.parseColor("#8dcfa8")   // 100%
    private val happyColorStart = Color.parseColor("#f9eeea")  // 0%
    private val happyColorEnd = Color.parseColor("#e9a69b")   // 100%
    private val colorEvaluator = ArgbEvaluator()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        val viewHolder = MonthViewHolder(view)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MonthViewHolder, @SuppressLint("RecyclerView") position: Int) {
        RetrofitClient.api.getTodoID(id).enqueue(object :Callback<List<ToDoDTO>>{
            override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                val months = response.body() ?: listOf()
                val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                val monthBlock = months.filter {
                    val currentPosition = position + 1
                    val currentMonth = monthNames.getOrNull(currentPosition - 1)
                    Log.d("Filter", "Position: $currentPosition")
                    it.year == year.toString() && it.month == currentMonth.toString()
                }
                val monthStatusValues = monthBlock.mapNotNull { it.status.toFloat() }
                val monthStatusAverage = if (monthBlock.isNotEmpty()) monthStatusValues.average() else 0.0

                val monthFeelValues = monthBlock.mapNotNull { it.feel.toFloat() }
                val monthFeelAverage = if (monthFeelValues.isNotEmpty()) monthFeelValues.average() else 10.0


                val color = getProgressColor(monthStatusAverage,monthFeelAverage)
                holder.monthview.setBackgroundColor(color)
            }

            override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                Log.e("ERROR", "MonthAdapter error")
            }
        })
    }

    override fun getItemCount(): Int = 12

    private fun getProgressColor(statusAvg: Double, feelAvg: Double): Int {
        return when {
            feelAvg >= 0.0f && feelAvg <= 1.0f -> colorEvaluator.evaluate(statusAvg.toFloat(),sadColorStart,sadColorEnd) as Int
            feelAvg > 1.0f && feelAvg <= 2.0f -> colorEvaluator.evaluate(statusAvg.toFloat(),sosoColorStart,sosoColorEnd) as Int
            feelAvg > 2.0f && feelAvg <= 3.0f -> colorEvaluator.evaluate(statusAvg.toFloat(),goodColorStart,goodColorEnd) as Int
            feelAvg > 3.0f && feelAvg <= 4.0f -> colorEvaluator.evaluate(statusAvg.toFloat(),happyColorStart,happyColorEnd) as Int
            else -> return goodColorStart }
    }

    fun refreshData() {
        notifyDataSetChanged()
    }

}
