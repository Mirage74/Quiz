package com.balex.quiz.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "countries_not_used_in_quiz")
data class CountryNotUsedInQuiz (
    @PrimaryKey
    @SerializedName("ID")
    val id: Int,
    @SerializedName("COUNTRY_NAME")
    val countryName: String,
    @SerializedName("CAPITAL_NAME")
    val capitalName: String,
    @SerializedName("DIFFICULT_LVL")
    val difficultLevel: Int,
    @SerializedName("IMAGE_NAME")
    val imageName: String
)