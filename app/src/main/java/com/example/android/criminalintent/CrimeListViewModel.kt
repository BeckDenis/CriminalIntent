package com.example.android.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0..99) {
            crimes += Crime(title = "Crime: #$i", isSolved = i % 2 == 0)
        }
    }
}