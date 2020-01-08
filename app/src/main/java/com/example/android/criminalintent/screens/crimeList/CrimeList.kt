package com.example.android.criminalintent.screens.crimeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.criminalintent.R
import com.example.android.criminalintent.database.Crime
import kotlinx.android.synthetic.main.list_item_crime.view.*
import java.text.DateFormat

class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(crime: Crime) {
        itemView.crime_title.text = crime.title
        itemView.crime_date.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
        itemView.crime_solved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class CrimeAdapter(val clickListener: (Crime) -> Unit) :
    ListAdapter<Crime, CrimeHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Crime>() {
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id == newItem.id
        }
    }

    fun addHeaderAndSubmitList(list: List<Crime>?) {
        val items = when (list) {
            null -> listOf(Crime(id = -1))
            else -> listOf(Crime(id = -1)) + list.reversed()
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return CrimeHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.header else R.layout.list_item_crime
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = getItem(position)
        holder.itemView.setOnClickListener { clickListener(crime) }
        if (position != 0) holder.bind(crime)
    }
}