package com.example.android.criminalintent.screens.crimeList


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.criminalintent.R
import com.example.android.criminalintent.database.Crime
import com.example.android.criminalintent.database.CrimeDatabase
import kotlinx.android.synthetic.main.fragment_crime_list.view.*
import kotlinx.android.synthetic.main.list_item_crime.view.*

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
            findNavController().navigate(CrimeListFragmentDirections.actionCrimeListFragmentToCrimeDetailFragment(it.id))
        }

        view.crime_recycler_view.run {
            layoutManager = LinearLayoutManager(context)
            adapter = crimeAdapter
        }

        viewModel.crimes.observe(viewLifecycleOwner, Observer {
            it?.let {
                crimeAdapter.data = it
            }
        })

        return view
    }


}
