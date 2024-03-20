package com.balex.quiz.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.balex.quiz.R
import com.balex.quiz.domain.entity.Country

class CountriesAdapter: ListAdapter<Country, CountryViewHolder>(CountryDiffCallback()) {
    val TAG = "CountriesAdapter"
    var onCountryItemClickListener: ((Country) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        Log.d(TAG, "onCreateViewHolder")
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country: Country = getItem(position)
        Log.d(TAG, "onBindViewHolder position: $position")
        holder.view.setOnClickListener {
            onCountryItemClickListener?.invoke(country)
        }
        holder.bind(country)
    }
}