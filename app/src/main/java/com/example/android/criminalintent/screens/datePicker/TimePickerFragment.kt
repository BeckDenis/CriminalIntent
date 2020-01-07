package com.example.android.criminalintent.screens.datePicker


import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.criminalintent.shareViewModels.SharedViewModel
import java.util.*

class TimePickerFragment : DialogFragment() {
    private lateinit var model: SharedViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val dateListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hourOfDay: Int, minute: Int->
            val date = model.selected.value ?: Date()
            val calendar = Calendar.getInstance()
            calendar.run {
                time = date
                set(Calendar.MINUTE, minute)
                set(Calendar.HOUR_OF_DAY, hourOfDay)
            }
            model.select(calendar.time)
        }

        val date = model.selected.value ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            dateListener,
            initialHourOfDay,
            initialMinute,
            false
        )
    }
}