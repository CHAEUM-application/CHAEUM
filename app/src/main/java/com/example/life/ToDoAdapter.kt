package com.example.life

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoAdapter(
    val todos: MutableList<ToDo>,
    private val progressBar: ProgressBar,
    private val feelView: TextView,
    private val u_id: String,
    private val year: String,
    private val month: String,
    private val week: String,
    private val status: Int,
    private val feel: Int,
    private val context: Context

) : RecyclerView.Adapter<ToDoViewHolder>() {
    private var selectedEmotion: Int = 0
    private lateinit var emotionListener: AdapterView.OnItemSelectedListener
    private lateinit var emotionsAdapter: ArrayAdapter<String>

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        val viewHolder = ToDoViewHolder(view)
        // 리사이클러뷰 안에 감정 콤보박스에 데이터 연결
        emotionsAdapter = ArrayAdapter(parent.context, android.R.layout.simple_spinner_item,
            parent.context.resources.getStringArray(R.array.emotion))
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emotionListener = object : AdapterView.OnItemSelectedListener {
            // 감정 콤보 박스가 선택될 시
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedEmotion = pos
                val text = viewHolder.itemView.findViewById<EditText>(R.id.editText).text.toString()
                val position = todos.indexOfFirst { it.text == text }
                todos[position].emotion = pos
                RetrofitClient.api.updTodoInfo(u_id,year, month, week, text, text, 5, selectedEmotion).enqueue(object : Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        // 감정 변경 완료
                        Log.d("TAG", "넘어감")
                        Log.d("TAG", text)
                        Log.d("TAG", pos.toString())
                        progressBar.progress = getCompletedTodosCount()
                        var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
                        val feelAvg =  calculateFeelAverage()
                        Log.d("TAGnowChange", feelAvg.toString())
                        // UI 업데이트를 Handler를 통해 스케줄링
                        feelProgressText(feelAvg, progressAvg)
                        return
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        return
                    }
                })
                var feelAvg = calculateFeelAverage()
                progressBar.max = todos.size
                progressBar.progress = getCompletedTodosCount()
                var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
                Log.d("TAGfeel", feelAvg.toString())
                feelProgressText(feelAvg, progressAvg)
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
    val handler = Handler(Looper.getMainLooper())

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.emotionBox.adapter = emotionsAdapter
        holder.emotionBox.onItemSelectedListener = emotionListener
        holder.emotionBox.setSelection(todo.emotion)

        // Initialize progress bar
        progressBar.max = todos.size
        progressBar.progress = getCompletedTodosCount()
        var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
        var feelAvg = calculateFeelAverage()
        Log.d("TAGcur", feelAvg.toString())
        feelProgressText(feelAvg,progressAvg)
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
        holder.editText.setPadding(25, 0, 0, 0)

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
            RetrofitClient.api.updTodoInfo(u_id, year, month, week, text, text, r_status, feel)
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        // status 1로 변경 완료
                        progressBar.progress = getCompletedTodosCount()
                        var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
                        Log.d("TAGpro", feelAvg.toString())
                        val feelAvg = calculateFeelAverage()
                        // UI 업데이트를 Handler를 통해 스케줄링
                        handler.post {
                            feelProgressText(feelAvg, progressAvg)
                        }
                        return
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        return
                    }

                })
            progressBar.progress = getCompletedTodosCount()
            progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
            Log.d("TAGpro", feelAvg.toString())
            feelProgressText(feelAvg,progressAvg)
        }

        //holder.editText.isEnabled = !todo.isDone

        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                todo.text = s.toString()
            }
        })

//        fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//            selectedEmotion = pos
//            todos[position].emotion = pos
//            Log.d("TAG",todos[position].emotion.toString())
//            RetrofitClient.api.updTodoInfo(u_id,year, month, week, todo.text, todo.text, 5, selectedEmotion).enqueue(object : Callback<Unit>{
//                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                    // 감정 변경 완료
//                    Log.d("TAG", "넘어감")
//                    Log.d("TAG", todo.text)
//                    Log.d("TAG", pos.toString())
//                    todos[position].emotion = pos
//                    progressBar.progress = getCompletedTodosCount()
//                    var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
//                    val feelAvg =  calculateFeelAverage()
//                    Log.d("TAGnowChange", feelAvg.toString())
//                    // UI 업데이트를 Handler를 통해 스케줄링
//                    feelProgressText(feelAvg, progressAvg)
//                    return
//                }
//                override fun onFailure(call: Call<Unit>, t: Throwable) {
//                    return
//                }
//            })
//            todos[position].emotion = pos
//            Log.d("TAG",todos[position].emotion.toString())
//            var feelAvg = calculateFeelAverage()
//            progressBar.max = todos.size
//            progressBar.progress = getCompletedTodosCount()
//            var progressAvg = (progressBar.progress.toFloat() / progressBar.max * 100).toInt()
//            Log.d("TAGfeel", feelAvg.toString())
//            feelProgressText(feelAvg, progressAvg)
//        }

        holder.doneButton.setOnClickListener {
            // 수정아닐땐 -> 텍스트 못건드림 수정누르면 값가져옴
            // 1일때는 텍스트 수정 가능, 버튼 = Enter
            //if (verify == 1){
            if (holder.doneButton.text.toString() == "MODIFY") {
                verify *= -1
                res_text = holder.editText.text.toString()
                holder.editText.isEnabled = true
                holder.editText.requestFocus()
                val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

                holder.doneButton.text = "ENTER"
                holder.doneButton.isSelected = true

            }
            // -1일대는 텍스트 수정 불가, 버튼 = modify
            //else if (verify == 1){
            else if (holder.doneButton.text.toString() == "ENTER") {
                verify *= -1
                req_text = holder.editText.text.toString()
                holder.editText.isEnabled = false
                holder.doneButton.text = "MODIFY"
                holder.doneButton.isSelected = false


                if (req_text == res_text) {
                    return@setOnClickListener
                } else {
                    RetrofitClient.api.updTodoInfo(u_id, year, month, week, req_text, res_text, -1, feel)
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

    fun calculateFeelAverage(): Int {
        if (todos.isEmpty()) {
            return 0
        }
        var totalFeel = 0
        var totalTodo = 0
        for (todo in todos) {
            totalFeel += todo.emotion
            totalTodo++
        }
        return if (totalTodo > 0) (totalFeel.toFloat() / totalTodo*25).toInt() else 0
    }

    fun feelProgressText(feelAvg: Int, progress: Int) {
        feelView.text = "행복도: ${feelAvg}%   진행도: ${progress}%"
    }
}
