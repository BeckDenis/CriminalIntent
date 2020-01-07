package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDao
import kotlinx.coroutines.*

class CrimeListViewModel(val database: CrimeDao) : ViewModel() {
    val crimes = database.getCrimes()
}