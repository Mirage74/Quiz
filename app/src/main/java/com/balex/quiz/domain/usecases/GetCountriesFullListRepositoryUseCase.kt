package com.balex.quiz.domain.usecases

import androidx.lifecycle.LiveData
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.repository.QuizRepository

class GetCountriesFullListRepositoryUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(): LiveData<List<Country>> {
        return quizRepository.getCountriesListFullRepository()
    }
}