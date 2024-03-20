package com.balex.quiz.presentation

import androidx.recyclerview.widget.DiffUtil
import com.balex.quiz.domain.entity.Country

class CountryDiffCallback: DiffUtil.ItemCallback<Country>() {

    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}