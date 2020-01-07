package com.example.android.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.criminalintent.screens.crimeDetail.CrimeFragment
import com.example.android.criminalintent.screens.crimeList.CrimeListFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        }
}
