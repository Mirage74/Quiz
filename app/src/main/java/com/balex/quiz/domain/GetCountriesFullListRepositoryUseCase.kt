package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.pojo.Country

class GetCountriesFullListRepositoryUseCase(private val quizRepository: QuizRepository) {
    fun getCountriesFullList(): LiveData<List<Country>> {
        return quizRepository.getCountriesListFullRepository()
    }
}