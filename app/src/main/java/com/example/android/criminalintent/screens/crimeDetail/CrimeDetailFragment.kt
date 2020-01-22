package com.example.android.criminalintent.screens.crimeDetail


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.android.criminalintent.shareViewModels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_crime_detail.*
import java.text.DateFormat
private const val REQUEST_CONTACT = 1
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
        crime_report.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.crime_report_subject))
            }.also { intent ->
                val chooserIntent =
                    Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }
        crime_suspect.apply {
            val pickContactIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crime.title = crime_title.text.toString()
        viewModel.updateCrime(crime)
    }

    private fun updateUI() {
        crime_title.setText(crime.title)
        crime_date.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
        crime_time.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(crime.date)
        crime_solved.isChecked = crime.isSolved
        if (crime.suspect.isNotEmpty()) {
            crime_suspect.text = crime.suspect
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                // Specify which fields you want your query to return values for
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                // Perform your query - the contactUri is like a "where" clause here
                val cursor = contactUri?.let {
                    requireActivity().contentResolver
                        .query(it, queryFields, null, null, null)
                }
                cursor?.use {
                    // Verify cursor contains at least one result
                    if (it.count == 0) {
                        return
                    }
                    // Pull out the first column of the first row of data -
                    // that is your suspect's name
                    it.moveToFirst()
                    val suspect = it.getString(0)
                    crime.suspect = suspect
                    viewModel.updateCrime(crime)
                    crime_suspect.text = suspect
                }
            }
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(crime.date)
        val suspect = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report,
            crime.title, dateString, solvedString, suspect)
    }
}
