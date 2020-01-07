package com.example.android.criminalintent.screens.crimeDetail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.criminalintent.R
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDatabase
import kotlinx.android.synthetic.main.fragment_crime_detail.*

class CrimeFragment : Fragment() {
    private lateinit var crime: Crime
    private lateinit var viewModel: CrimeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_detail, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = CrimeDatabase.getInstance(application).crimeDatabaseDao
        val arguments = CrimeFragmentArgs.fromBundle(arguments!!)
        val viewModelFactory = CrimeDetailViewModelFactory(dataSource, arguments.crimeId)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CrimeDetailViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crime.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        crime_solved.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }

        crime_date.setOnClickListener {
            findNavController().navigate(R.id.datePickerFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        crime.title = crime_title.text.toString()
        viewModel.updatePlayer(crime)
    }

    private fun updateUI() {
        crime_title.setText(crime.title)
        crime_date.text = crime.date
        crime_solved.isChecked = crime.isSolved
    }
}
