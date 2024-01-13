package com.balex.quiz.data.pojo

import com.google.gson.annotations.SerializedName

data class UserScoreResponse(
    @SerializedName("user")
    val userScore: UserScore
)
