package com.example.android.criminalintent.shareViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Date>()

    fun select(item: Date) {
        selected.value = item
    }
}