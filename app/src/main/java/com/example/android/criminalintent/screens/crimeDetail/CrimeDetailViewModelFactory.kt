package com.example.android.criminalintent.screens.crimeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.criminalintent.database.CrimeDao

class CrimeDetailViewModelFactory(private val dataSource: CrimeDao, private val crimeId: Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrimeDetailViewModel::class.java)) {
            return CrimeDetailViewModel(dataSource, crimeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}