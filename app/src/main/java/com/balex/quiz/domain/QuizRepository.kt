package com.balex.quiz.domain

import androidx.lifecycle.LiveData
import com.balex.quiz.data.database.CountriesDao
import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.Country
import io.reactivex.rxjava3.core.Single

interface QuizRepository {
    fun getCountriesListRepository(): Single<CountriesResponse>
    fun getCountriesListNotUsedRepository(dbDAO : CountriesDao): LiveData<List<Country>>
}