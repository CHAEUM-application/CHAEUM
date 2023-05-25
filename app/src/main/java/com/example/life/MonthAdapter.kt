package com.example.life

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter : RecyclerView.Adapter<MonthViewHolder>() {

    private val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val weeksInMonth = listOf(4, 4, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5) // This is an approximation. You might need a more accurate calculation.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        val viewHolder = MonthViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.monthView.text = months[position]

        // Post a task to be run after layout
        holder.weeksRecyclerView.post {

            // Calculate the number of weeks that can fit in the RecyclerView
            holder.weeksRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.weeksRecyclerView.adapter = WeekAdapter(weeksInMonth[position])
        }
    }

    override fun getItemCount(): Int = months.size // This is the number of months in the year
}
