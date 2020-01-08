package com.example.android.criminalintent.screens.crimeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDao
import kotlinx.coroutines.*

class CrimeListViewModel(val database: CrimeDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val crimes = database.getCrimes()
    private var helperCrime: Crime? = null

    private val _lastCrime = MutableLiveData<Crime>()
    val lastCrime: LiveData<Crime>
        get() = _lastCrime

    fun addCrime() {
        uiScope.launch {
            insertCrime()
        }
    }

    private suspend fun insertCrime() {
        withContext(Dispatchers.IO) {
            database.addCrime(Crime())
            helperCrime = database.getLastCrime()
            changeLastCrime()
        }
    }

    private fun changeLastCrime() {
        uiScope.launch {
            _lastCrime.value = helperCrime
        }
    }

    fun doneNavigating() {
        _lastCrime.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}