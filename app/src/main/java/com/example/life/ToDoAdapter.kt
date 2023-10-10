package com.example.life

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoAdapter(
    val todos: MutableList<ToDo>,
    private val progressBar: ProgressBar,
    private val id: String,
    private val year: String,
    private val month: String,
    private val week: String,
    private val status: Int,
    private val feel: Int

) : RecyclerView.Adapter<ToDoViewHolder>() {
    private var selectedEmotion: Int = 0
    private lateinit var emotionListener: AdapterView.OnItemSelectedListener
    private lateinit var emotionsAdapter: ArrayAdapter<String>

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
//        emotionsAdapter = ArrayAdapter(parent.context, R.array.emotion)
        // 리사이클러뷰 안에 감정 콤보박스에 데이터 연결
        emotionsAdapter = ArrayAdapter(parent.context, android.R.layout.simple_spinner_item,
            parent.context.resources.getStringArray(R.array.emotion))
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emotionListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedEmotion = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        return ToDoViewHolder(view)
    }

    // 1일때 Enter, -1일때 modify
    var verify: Int = 1
    var res_text: String = ""
    var req_text: String = ""
    var r_status: Int = 0

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.emotionBox.adapter = emotionsAdapter
        holder.emotionBox.onItemSelectedListener = emotionListener
        holder.emotionBox.setSelection(todo.emotion)

        // Initialize progress bar
        progressBar.max = todos.size
        progressBar.progress = getCompletedTodosCount()

        if(todo.text == "") {
            holder.doneButton.text = "ENTER"
            holder.editText.isEnabled = true
        }
        else{
            holder.doneButton.text = "MODIFY"
            holder.editText.isEnabled = false
        }
        holder.checkBox.text = ""
        holder.checkBox.isChecked = todo.isDone
        holder.editText.setText(todo.text)

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = todo.isDone

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
            if (isChecked) {
                holder.checkBox.text = ""
                r_status = 1
            } else {
                holder.checkBox.text = ""
                r_status = 0
            }
            val text = holder.editText.text.toString()
            RetrofitClient.api.updTodoInfo(id, year, month, week, text, text, r_status, feel)
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        // status 1로 변경 완료
                        return
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        return
                    }

                })
            progressBar.progress = getCompletedTodosCount()
        }

        //holder.editText.isEnabled = !todo.isDone

        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                todo.text = s.toString()
            }
        })

        holder.doneButton.setOnClickListener {
            // 수정아닐땐 -> 텍스트 못건드림 수정누르면 값가져옴
            // 1일때는 텍스트 수정 가능, 버튼 = Enter
            //if (verify == 1){
            if (holder.doneButton.text.toString() == "MODIFY") {
                verify *= -1
                res_text = holder.editText.text.toString()
                holder.editText.isEnabled = true
                holder.doneButton.text = "ENTER"

            }
            // -1일대는 텍스트 수정 불가, 버튼 = modify
            //else if (verify == 1){
            else if (holder.doneButton.text.toString() == "ENTER") {
                verify *= -1
                req_text = holder.editText.text.toString()
                holder.editText.isEnabled = false
                holder.doneButton.text = "MODIFY"
                if (req_text == res_text) {
                    return@setOnClickListener
                } else {
                    RetrofitClient.api.updTodoInfo(id, year, month, week, req_text, res_text, -1, feel)
                        .enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                // text 변경 완료
                                return
                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                return
                            }
                        })
                }
            }
        }
    }

    override fun getItemCount(): Int = todos.size

    fun addTodo(todo: ToDo) {
        todos.add(0, todo)
        notifyItemInserted(0)
        progressBar.max = todos.size // Update the total count in the progress bar
        progressBar.progress = getCompletedTodosCount()
    }

    fun removeTodo(position: Int) {
        val removedTodo = todos.removeAt(position)
        notifyDataSetChanged()
        progressBar.max = todos.size // Update the total count in the progress bar
        progressBar.progress = getCompletedTodosCount()
    }

    fun getCompletedTodosCount(): Int = todos.count { it.isDone }

    fun getTodosCount(): Int = todos.size

    fun getItemText(position: Int): String {
        return todos[position].text
    }
}
