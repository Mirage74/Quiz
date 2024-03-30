package com.balex.quiz.domain.entity

data class UserAnswer(
    val frameNum: Int = 0,
    val questionId: Int = 0,
    val answerId: Int = 0,
    val score: Int = 0
)