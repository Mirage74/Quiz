package com.balex.quiz.domain.entity

import java.util.Arrays

data class Question(
    val id: Int,
    val countryName: String,
    val imageName: String,
    val option1Id: Int,
    val option2Id: Int,
    val option3Id: Int,
    val option4Id: Int,
    val rightAnswerNumOption: Int
) {
    fun getOptionId(option: Int): Int {
        return when(option) {
            1 -> option1Id
            2 -> option2Id
            3 -> option3Id
            else -> option4Id
        }
    }
}