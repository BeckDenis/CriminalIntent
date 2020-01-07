package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDao
import kotlinx.coroutines.*
import java.util.*

class CrimeListViewModel(val database: CrimeDao) : ViewModel() {
    val crimes = database.getCrimes()

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        repeat(10) {
            addCrime()
        }
    }

    private fun addCrime() {
        uiScope.launch {
            insertCrime()
        }
    }

    private suspend fun insertCrime() {
        withContext(Dispatchers.IO) {
            database.addCrime(Crime(title = "title"))
        }
    }
}