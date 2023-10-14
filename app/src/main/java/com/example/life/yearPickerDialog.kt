package com.example.life

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.Year

class yearPickerDialog (private val year: Int, private val age: Int, val itemClick: (Int) -> Unit) : DialogFragment(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.year_dialog, null)

        val yearList = dialogView.findViewById<NumberPicker>(R.id.picker_year)
        yearList.minValue = Year.now().value-age+1
        yearList.maxValue = Year.now().value-age+91
        yearList.value = year

        val confirm = dialogView.findViewById<Button>(R.id.btn_confirm)
        val cancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        confirm.setOnClickListener {
            val pickYear = yearList.value
            itemClick(pickYear)
            dismiss()
        }
        cancel.setOnClickListener {
            dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setView(dialogView)

        return alertDialog
    }
}