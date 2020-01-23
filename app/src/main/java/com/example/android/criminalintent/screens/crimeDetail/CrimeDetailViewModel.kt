package com.example.android.criminalintent.screens.crimeDetail


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDao
import kotlinx.coroutines.*
import java.io.File

class CrimeDetailViewModel(val database: CrimeDao, crimeId: Long, context: Context) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val crime = database.getCrime(crimeId)
    private val filesDir = context.applicationContext.filesDir

    fun updateCrime(crime: Crime) {
        uiScope.launch {
            update(crime)
        }
    }

    private suspend fun update(crime: Crime) {
        withContext(Dispatchers.IO) {
            database.updateCrime(crime)
        }
    }

    fun getPhotoFile(crime: Crime): File = File(filesDir, crime.photoFileName)
}