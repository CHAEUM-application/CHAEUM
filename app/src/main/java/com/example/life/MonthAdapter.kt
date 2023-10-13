package com.example.life

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthAdapter(
    private val year: Int,
    private val id: String,
    private val context: Context
) : RecyclerView.Adapter<MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        val viewHolder = MonthViewHolder(view)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        RetrofitClient.api.getTodoID(id).enqueue(object :Callback<List<ToDoDTO>>{
            override fun onResponse(call: Call<List<ToDoDTO>>, response: Response<List<ToDoDTO>>) {
                val months = response.body() ?: listOf()
            }

            override fun onFailure(call: Call<List<ToDoDTO>>, t: Throwable) {
                Log.e("ERROR", "MonthAdapter error")
            }
        })
    }

    override fun getItemCount(): Int = 12

    fun refreshData() {
        notifyDataSetChanged()
    }
}
