package com.balex.quiz.domain.entity

data class Question(
    val countryName: String,
    val option_1_Id: Int,
    val option_2_Id: Int,
    val option_3_Id: Int,
    val option_4_Id: Int,
    val rightAnswerNumOption: Int

)