package com.example.life

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter(private val year: Int, private val id: String) : RecyclerView.Adapter<MonthViewHolder>() {

    private val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val weeksPerMonth = listOf(4, 4, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        val viewHolder = MonthViewHolder(view)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.monthView.text = months[position]

        // Use the weeksPerMonth list instead of calculateWeeksInMonth
        val numberOfWeeks = weeksPerMonth[position]

        holder.weeksRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.weeksRecyclerView.adapter = WeekAdapter(numberOfWeeks, holder.itemView.context, year, months[position], id)
    }

    override fun getItemCount(): Int = months.size
}
