package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.CrimeRepository

class CrimeListViewModel : ViewModel() {
    private val crimeRepository =
        CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}