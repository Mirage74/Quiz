package com.balex.quiz.domain

import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.Country
import io.reactivex.rxjava3.core.Single

class GetCountriesListRepositoryUseCase(private val quizRepository: QuizRepository) {
    fun getCountriesList(): Single<CountriesResponse> {

        return quizRepository.getCountriesListRepository()
    }
}