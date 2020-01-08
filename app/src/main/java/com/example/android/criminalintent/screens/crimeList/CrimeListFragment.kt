package com.example.android.criminalintent.screens.crimeList


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.criminalintent.R
import com.example.android.criminalintent.database.CrimeDatabase
import kotlinx.android.synthetic.main.fragment_crime_list.view.*

class CrimeListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = CrimeDatabase.getInstance(application).crimeDatabaseDao
        val viewModelFactory = CrimeListViewModelFactory(dataSource)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CrimeListViewModel::class.java)
        val crimeAdapter = CrimeAdapter {
            findNavController().navigate(
                CrimeListFragmentDirections.actionCrimeListToCrimeDetail(it.id)
            )
        }

        viewModel.lastCrime.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    CrimeListFragmentDirections.actionCrimeListToCrimeDetail(it.id)
                )
                viewModel.doneNavigating()
            }
        })

        view.crime_recycler_view.run {
            layoutManager = LinearLayoutManager(context)
            adapter = crimeAdapter
        }

        view.fab.setOnClickListener {
            viewModel.addCrime()
        }

        viewModel.crimes.observe(viewLifecycleOwner, Observer {
            it?.let {
                crimeAdapter.addHeaderAndSubmitList(it)
                view.crime_recycler_view.scrollToPosition(0)
                view.place_holder.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        })

        return view
    }
}
