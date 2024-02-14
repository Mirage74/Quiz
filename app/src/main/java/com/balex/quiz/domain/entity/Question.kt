package com.balex.quiz.domain.entity

import android.widget.ImageView

data class Question(
    val countryName: String,
    val option1Id: Int,
    val option2Id: Int,
    val option3Id: Int,
    val option4Id: Int,
    val rightAnswerNumOption: Int

) {
    lateinit var imageView: ImageView
}