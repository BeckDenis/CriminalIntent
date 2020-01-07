package com.example.android.criminalintent.screens.datePicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.criminalintent.shareViewModels.SharedViewModel
import java.util.*

class DatePickerFragment : DialogFragment() {
    private lateinit var model: SharedViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val date = model.selected.value ?: Date()
                val calendar = Calendar.getInstance()
                calendar.run {
                    time = date
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                }
                model.select(calendar.time)
            }

        val date = model.selected.value ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
}