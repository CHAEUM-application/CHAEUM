package com.example.life

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

// inside onCreateViewHolder

class WeekAdapter(private val numberOfWeeks: Int) : RecyclerView.Adapter<WeekViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.weekView.setBackgroundColor(Color.LTGRAY)  // Or whatever color you want
    }

    override fun getItemCount(): Int = numberOfWeeks

}

