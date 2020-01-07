package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.criminalintent.database.CrimeDao

class CrimeListViewModelFactory(private val dataSource: CrimeDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrimeListViewModel::class.java)) {
            return CrimeListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

