package com.balex.quiz.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey
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
