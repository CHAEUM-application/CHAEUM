package com.example.life

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val monthText: TextView = view.findViewById(R.id.monthtext)
    val monthRecyler: RecyclerView = view.findViewById(R.id.monthRecycler)

}
