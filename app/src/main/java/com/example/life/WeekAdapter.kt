package com.example.life

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeekAdapter(private val numberOfWeeks: Int,
                  private val context: Context,
                  private val year: Int,
                  private val month: String,
                  private val id: String) : RecyclerView.Adapter<WeekViewHolder>() {

    private val colorStart = Color.parseColor("#eafaf1")  // 0%
    private val colorMid = Color.parseColor("#97e7b9")   // 50%
    private val colorEnd = Color.parseColor("#44d580")   // 100%
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
                val doneTodos = weekTodos.count { it.status == 1 }
                val progress = if (weekTodos.isNotEmpty()) doneTodos / weekTodos.size.toFloat() else 0f

                val color = getProgressColor(progress)
                holder.weekView.setBackgroundColor(color)
            }

            override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                // handle error
            }
        })
    }

    override fun getItemCount(): Int = numberOfWeeks

    private fun getProgressColor(progress: Float): Int {
        return if (progress < 0.5) {
            // For progress from 0% to 50%, interpolate between colorStart (red) and colorMid (yellow)
            colorEvaluator.evaluate(progress * 2, colorStart, colorMid) as Int
        } else {
            // For progress from 50% to 100%, interpolate between colorMid (yellow) and colorEnd (green)
            colorEvaluator.evaluate((progress - 0.5f) * 2, colorMid, colorEnd) as Int
        }
    }

    fun refreshData() {
        notifyDataSetChanged()
    }
}

