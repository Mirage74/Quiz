package com.balex.quiz.domain.entity

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("id")
    val id: Int,
    @SerializedName("countryName")
    val countryName: String,
    @SerializedName("capitalName")
    val capitalName: String,
    @SerializedName("diffLvl")
    val difficultLevel: Int,
    @SerializedName("imageName")
    val imageName: String
)
