package com.example.life

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

/*
class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    val editText: EditText = itemView.findViewById(R.id.editText)
    val doneButton: Button = itemView.findViewById(R.id.doneButton)
    private var textWatcher: TextWatcher? = null

    init {
        checkBox.setOnCheckedChangeListener(null) // Add this line
    }

    fun bind(todo: ToDo) {
        checkBox.isChecked = todo.isDone
        editText.setText(todo.description)

        textWatcher?.let { editText.removeTextChangedListener(it) }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                todo.description = s.toString()
            }
        }

        editText.addTextChangedListener(textWatcher)

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
        }

        doneButton.setOnClickListener {
            editText.isEnabled = false
            doneButton.isEnabled = false
        }


    }
}
*/

class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    val editText: EditText = itemView.findViewById(R.id.editText)
    val doneButton: Button = itemView.findViewById(R.id.doneButton)

    init {
        checkBox.setOnCheckedChangeListener(null)

        editText.isFocusable = false
        editText.isClickable = false

        editText.setOnClickListener {
            editText.isFocusableInTouchMode = true
            editText.isClickable = true
            editText.isFocusable = true
            editText.requestFocus()
            doneButton.isEnabled = true
        }

        doneButton.setOnClickListener {
            editText.isFocusable = false
            editText.isClickable = false
            doneButton.isEnabled = false
        }
    }
}

