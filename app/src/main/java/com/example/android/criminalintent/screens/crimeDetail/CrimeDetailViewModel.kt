package com.example.android.criminalintent.screens.crimeDetail


import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDao
import kotlinx.coroutines.*

class CrimeDetailViewModel(val database: CrimeDao, private val crimeId: Long) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val crime = database.getCrime(crimeId)

    fun updatePlayer(crime: Crime) {
        uiScope.launch {
            update(crime)
        }
    }

    private suspend fun update(crime: Crime) {
        withContext(Dispatchers.IO) {
            database.updateCrime(crime)
        }
    }
}