package com.balex.quiz.data.entityExt

import com.balex.quiz.domain.entity.Question

class QuestionExt(val question: Question) {
    fun getOptionId(option: Int): Int {
        return when(option) {
            1 -> question.option1Id
            2 -> question.option2Id
            3 -> question.option3Id
            else -> question.option4Id
        }
    }
    fun isAnswerCorrect(i: Int): Boolean {
        return i == question.rightAnswerNumOption
    }
}