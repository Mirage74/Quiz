package com.balex.quiz.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.CountryItemBinding
import com.balex.quiz.domain.entity.Country
import com.bumptech.glide.Glide


class CountryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val binding = CountryItemBinding.bind(view)
    fun bind(country: Country) = with(binding){
        tvCountryName.text = country.countryName
        tvCapitalName.text = country.capitalName
        Glide.with(view)
            .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${country.imageName}")
            .into(imCapital)
    }
}