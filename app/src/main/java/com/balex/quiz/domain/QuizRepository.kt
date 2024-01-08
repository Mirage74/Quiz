package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.pojo.Country

interface QuizRepository {
    fun getCountriesListFullRepository(): LiveData<List<Country>>
    fun getCountriesListNotUsedRepository(): LiveData<List<Country>>
    fun deleteCountryFromNotUsedListRepository(country: Country)
}