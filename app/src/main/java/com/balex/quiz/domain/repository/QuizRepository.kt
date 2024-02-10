package com.balex.quiz.domain.repository

import androidx.lifecycle.LiveData
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.entity.UserScore

interface QuizRepository {
    fun getCountriesListFullRepository(): LiveData<List<Country>>
    fun getGameSettings(level: Level): GameSettings
    fun generateQuestion(level: Level, questionNumber: Int): Question
    fun getUserScore(): LiveData<UserScore>
    fun setUserScore(userScore: UserScore)
}