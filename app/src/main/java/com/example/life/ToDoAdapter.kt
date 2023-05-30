package com.example.life

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView

/*
class ToDoAdapter(
    private val todos: MutableList<ToDo>
) : RecyclerView.Adapter<ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.checkBox.text = todo.description
        holder.checkBox.isChecked = todo.isDone

        holder.checkBox.setOnCheckedChangeListener(null) // Remove previous listener

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
            if (isChecked) {
                holder.checkBox.text = todo.description  // Set the text again when checked
            }
            notifyDataSetChanged()  // Update the adapter
        }
    }
    override fun getItemCount(): Int = todos.size
    fun addTodo(todo: ToDo) {
        todos.add(0, todo) // Add new items at the beginning of the list
        notifyItemInserted(0) // Notify the adapter that an item is inserted at position 0
    }
    fun removeTodo(position: Int) {
        val removedTodo = todos.removeAt(position)
        if (removedTodo.isDone) {
            removedTodo.description = ""  // Reset the description if the removed todo was checked
        }
        notifyDataSetChanged()
    }
    fun getCompletedTodosCount(): Int = todos.count { it.isDone }
    fun getTodosCount(): Int = todos.size
}
*/

/*
class ToDoAdapter(
    private val todos: MutableList<ToDo>,
    private val progressBar: ProgressBar
) : RecyclerView.Adapter<ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.checkBox.text = todo.description
        holder.checkBox.isChecked = todo.isDone
        holder.editText.setText(todo.description)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
            if (isChecked) {
                holder.checkBox.text = todo.description
            } else {
                holder.checkBox.text = ""
            }
            progressBar.progress = getCompletedTodosCount()
        }

        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                todo.description = s.toString()
                holder.checkBox.text = todo.description
            }
        })

        holder.doneButton.setOnClickListener {
            holder.editText.isEnabled = false
            holder.doneButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int = todos.size

    fun addTodo(todo: ToDo) {
        todos.add(0, todo)
        notifyItemInserted(0)
    }

    fun removeTodo(position: Int) {
        val removedTodo = todos.removeAt(position)
        if (removedTodo.isDone) {
            removedTodo.description = ""
        }
        notifyDataSetChanged()
        progressBar.progress = getCompletedTodosCount()
    }

    fun getCompletedTodosCount(): Int = todos.count { it.isDone }

    fun getTodosCount(): Int = todos.size
}
*/

class ToDoAdapter(
    private val todos: MutableList<ToDo>,
    private val progressBar: ProgressBar
) : RecyclerView.Adapter<ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.checkBox.text = todo.description
        holder.checkBox.isChecked = todo.isDone
        holder.editText.setText(todo.description)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
            if (isChecked) {
                holder.checkBox.text = todo.description
            } else {
                holder.checkBox.text = null
            }
            progressBar.progress = getCompletedTodosCount()
        }

        holder.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.doneButton.isEnabled = true
            }
        }

        holder.doneButton.setOnClickListener {
            holder.editText.isEnabled = false
            holder.editText.clearFocus()
            holder.doneButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int = todos.size

    fun addTodo(todo: ToDo) {
        todos.add(0, todo)
        notifyItemInserted(0)
    }

    fun removeTodo(position: Int) {
        val removedTodo = todos.removeAt(position)
        if (removedTodo.isDone) {
            removedTodo.description = ""
        }
        notifyDataSetChanged()
        progressBar.progress = getCompletedTodosCount()
    }

    fun getCompletedTodosCount(): Int = todos.count { it.isDone }

    fun getTodosCount(): Int = todos.size
}
