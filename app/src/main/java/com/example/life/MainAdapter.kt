package com.example.life

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.Year


class MainAdapter(
    private val birthYear: Int,
    private val id: String,
    private val years: IntArray = IntArray(90) { birthYear + it },
    private val context: Context
) : RecyclerView.Adapter<MainViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentYear = Year.now().value
        holder.yearView.setBackgroundColor(
            when {
                years[position] < currentYear -> {
                    holder.yearView.setOnClickListener {
                        val intent = Intent(context, YearActivity::class.java)
                        intent.putExtra("selectedYear", years[position]) // passing selected year to the YearActivity
                        intent.putExtra("id", id)
                        context.startActivity(intent)
                    }
                    Color.rgb(69, 219, 147)
                }
                years[position] == currentYear -> {
                    holder.yearView.setOnClickListener {
                        val intent = Intent(context, YearActivity::class.java)
                        intent.putExtra("selectedYear", years[position]) // passing selected year to the YearActivity
                        intent.putExtra("id", id)
                        context.startActivity(intent)
                    }
                    Color.YELLOW
                }
                else -> Color.WHITE
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = years.size
}

