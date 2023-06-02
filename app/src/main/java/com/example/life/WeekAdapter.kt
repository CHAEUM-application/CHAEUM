package com.example.life

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WeekAdapter(private val numberOfWeeks: Int,
                  private val context: Context,
                  private val year: Int,
                  private val month: String,
                  private val id: String) : RecyclerView.Adapter<WeekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        val viewHolder = WeekViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.weekView.setOnClickListener {
            val intent = Intent(context, ToDoActivity::class.java)
            intent.putExtra("selectedYear", year)
            intent.putExtra("selectedMonth", month)
            intent.putExtra("selectedWeek", position + 1)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
        holder.weekView.setBackgroundColor(Color.LTGRAY) // Set the color of the week box to light gray
    }

    override fun getItemCount(): Int = numberOfWeeks
}
