package com.balex.quiz.domain.repository

import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question

interface QuizRepository {
    fun getGameSettings(level: Level): GameSettings
    fun generateQuestion(
        level: Level, questionNumber: Int,
        countriesListFull: List<Country>,
        countriesListNotUsedInQuiz: List<Country>
    ): Question
}