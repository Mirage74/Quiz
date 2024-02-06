package com.balex.quiz.domain.entity

import com.google.gson.annotations.SerializedName

data class CountriesResponse(
    @SerializedName("caps")
    val countries: List<Country>
)
