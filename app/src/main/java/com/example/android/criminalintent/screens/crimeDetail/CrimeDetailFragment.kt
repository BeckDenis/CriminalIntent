package com.example.android.criminalintent.screens.crimeDetail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.criminalintent.R
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDatabase
import com.example.android.criminalintent.shareViewModels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_crime_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CrimeFragment : Fragment() {
    private lateinit var crime: Crime
    private lateinit var viewModel: CrimeDetailViewModel
    private lateinit var model: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
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
                    model.select(crime.date)
                    updateUI()
                }
            })
        model.selected.observe(
            viewLifecycleOwner,
            Observer { date ->
                date?.let {
                    crime.date = date
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

        crime_time.setOnClickListener {
            findNavController().navigate(R.id.timePickerFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        crime.title = crime_title.text.toString()
        viewModel.updatePlayer(crime)
    }

    private fun updateUI() {
        crime_title.setText(crime.title)
        crime_date.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
        crime_time.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(crime.date)
        crime_solved.isChecked = crime.isSolved
    }
}
