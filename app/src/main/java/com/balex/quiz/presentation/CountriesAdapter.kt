package com.balex.quiz.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.CountryItemBinding
import com.balex.quiz.domain.entity.Country
import com.bumptech.glide.Glide

class CountriesAdapter : ListAdapter<Country, CountryViewHolder>(CountryDiffCallback()) {
    //val TAG = "CountriesAdapter"

    var onCountryItemClickListener: OnCountryItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
//        return CountryViewHolder(view)


        val binding = CountryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding)


    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country: Country = getItem(position)

        with(holder.binding) {
            with(country) {
                tvCountryName.text = country.countryName
                tvCapitalName.text = country.capitalName
                Glide.with(root)
                    .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${country.imageName}")
                    .into(imCapital)
                root.setOnClickListener {
                    onCountryItemClickListener?.onCountryClick(this)
                }
            }
        }

    }

    interface OnCountryItemClickListener {
        fun onCountryClick(country: Country)
    }


}