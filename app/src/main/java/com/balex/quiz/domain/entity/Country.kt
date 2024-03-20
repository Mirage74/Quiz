package com.balex.quiz.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable
