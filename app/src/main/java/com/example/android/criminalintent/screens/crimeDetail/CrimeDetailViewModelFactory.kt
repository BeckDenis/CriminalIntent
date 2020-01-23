package com.example.android.criminalintent.screens.crimeDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.criminalintent.database.CrimeDao

class CrimeDetailViewModelFactory(private val dataSource: CrimeDao, private val crimeId: Long, private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrimeDetailViewModel::class.java)) {
            return CrimeDetailViewModel(dataSource, crimeId, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}