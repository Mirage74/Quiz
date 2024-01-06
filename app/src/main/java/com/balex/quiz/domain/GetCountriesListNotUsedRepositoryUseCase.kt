package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.pojo.Country

class GetCountriesListNotUsedRepositoryUseCase(private val quizRepository: QuizRepository) {
    fun getCountriesListNotUsed(): LiveData<List<Country>> {
        return quizRepository.getCountriesListNotUsedRepository()
    }
}