package com.balex.quiz.domain.entity

import com.google.gson.annotations.SerializedName

data class UserScoreResponse(
    @SerializedName("user")
    val userScore: UserScore
)
