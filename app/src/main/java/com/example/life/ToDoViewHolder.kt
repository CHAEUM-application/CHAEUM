package com.example.life

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView

class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    val emotionBox: Spinner = itemView.findViewById(R.id.emotionBox)
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

