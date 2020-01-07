package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.CrimeRepository
import com.example.android.criminalintent.database.Crime

class CrimeListViewModel : ViewModel() {
    private val crimeRepository =
        CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime) {
        crimeRepository.addCrime(crime)
    }
}