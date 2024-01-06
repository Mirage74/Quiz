package com.balex.quiz.data.pojo

import com.google.gson.annotations.SerializedName

data class UserScore(
    @SerializedName("DISPLAYNAME")
    val userName: String,
    @SerializedName("BESTSCORE")
    val bestScore: Int,
    @SerializedName("BEST_RES")
    val bestResultJSON: String,
    @SerializedName("LAST_RES")
    val lastResultJSON: String
)
