package com.balex.quiz.data.pojo

import com.google.gson.annotations.SerializedName

data class CountriesResponse(
    @SerializedName("caps")
    val countries: List<Country>
)
