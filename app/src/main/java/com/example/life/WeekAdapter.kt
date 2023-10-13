package com.example.life

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeekAdapter(private val numberOfWeeks: Int,
                  private val context: Context,
                  private val year: Int,
                  private val month: String,
                  private val id: String) : RecyclerView.Adapter<WeekViewHolder>() {

    // 0~1 sad(gray) 1~2 soso(orange) 2~3 good(green) 3~4 happy(pink)
    private val sadColorStart = Color.parseColor("#f5f3f2")  // 0%
    private val sadColorEnd = Color.parseColor("#a4928e")   // 100%
    private val sosoColorStart = Color.parseColor("#fdf7f1")  // 0%
    private val sosoColorEnd = Color.parseColor("#dec8b0")   // 100%
    private val goodColorStart = Color.parseColor("#f0f9f3")  // 0%
    private val goodColorEnd = Color.parseColor("#8dcfa8")   // 100%
    private val happyColorStart = Color.parseColor("#f9eeea")  // 0%
    private val happyColorEnd = Color.parseColor("#e9a69b")   // 100%
    private val colorEvaluator = ArgbEvaluator()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        val viewHolder = WeekViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeekViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.weekView.setOnClickListener {
            val intent = Intent(context, ToDoActivity::class.java)
            intent.putExtra("selectedYear", year)
            intent.putExtra("selectedMonth", month)
            intent.putExtra("selectedWeek", position + 1)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }

        RetrofitClient.api.getTodoID(id).enqueue(object : Callback<List<ToDoDTO>> {
            override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                val todos = response.body() ?: listOf()
                val weekTodos = todos.filter {
                    it.year == year.toString() && it.month == month && it.week == (position + 1).toString()
                }
                val weekFeelValues = weekTodos.mapNotNull { it.feel.toFloat() }
                val weekFeelAverage = if (weekFeelValues.isNotEmpty()) weekFeelValues.average() else 0.0

                Log.d("TAG",month)
                Log.d("TAG",weekFeelAverage.toString())
                val doneTodos = weekTodos.count { it.status == 1 }
                val progress = if (weekTodos.isNotEmpty()) doneTodos / weekTodos.size.toFloat() else 0f

                val color = getProgressColor(progress, weekFeelAverage)
                holder.weekView.setBackgroundColor(color)
            }

            override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                // handle error
            }
        })
    }

    override fun getItemCount(): Int = numberOfWeeks

    private fun getProgressColor(progress: Float, feelAvg: Double): Int {
        return when {
                feelAvg > 0.0f && feelAvg <= 1.0f -> colorEvaluator.evaluate(progress,sadColorStart,sadColorEnd) as Int
                feelAvg > 1.0f && feelAvg <= 2.0f -> colorEvaluator.evaluate(progress,sosoColorStart,sosoColorEnd) as Int
                feelAvg > 2.0f && feelAvg <= 3.0f -> colorEvaluator.evaluate(progress,goodColorStart,goodColorEnd) as Int
                feelAvg > 3.0f && feelAvg <= 4.0f -> colorEvaluator.evaluate(progress,happyColorStart,happyColorEnd) as Int
            else -> return goodColorStart }
        }

    fun refreshData() {
        notifyDataSetChanged()
    }
}

