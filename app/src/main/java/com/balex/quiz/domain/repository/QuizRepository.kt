package com.balex.quiz.domain.repository

import androidx.lifecycle.LiveData
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question

interface QuizRepository {
    fun getCountriesListFullRepository(): LiveData<List<Country>>
    fun getCountriesListNotUsedRepository(): LiveData<List<Country>>
    fun deleteCountryFromNotUsedListRepository(country: Country)
    fun generateQuestion(level: Level, questionNumber: Int): Question
}