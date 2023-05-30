package com.example.life

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val monthView: TextView = view.findViewById(R.id.monthView)
    val weeksRecyclerView: RecyclerView = view.findViewById(R.id.weeksRecyclerView)
}
