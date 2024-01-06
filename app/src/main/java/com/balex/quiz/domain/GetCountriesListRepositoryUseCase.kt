package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.Country
import io.reactivex.rxjava3.core.Single

class GetCountriesListRepositoryUseCase(private val quizRepository: QuizRepository) {
    fun getCountriesList(): LiveData<List<Country>> {
        return quizRepository.getCountriesListRepository()
    }
}