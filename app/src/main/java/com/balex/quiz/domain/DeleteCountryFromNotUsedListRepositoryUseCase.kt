package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.pojo.Country

class DeleteCountryFromNotUsedListRepositoryUseCase (private val quizRepository: QuizRepository) {
    fun deleteCountryFromNotUsedList(country: Country){
        quizRepository.deleteCountryFromNotUsedListRepository(country)
    }
}