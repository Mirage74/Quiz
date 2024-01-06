package com.balex.quiz.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.database.CountriesDao
import com.balex.quiz.data.database.CountriesDatabase
import com.balex.quiz.data.pojo.CountriesResponse
import com.balex.quiz.data.pojo.Country
import com.balex.quiz.domain.QuizRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object QuizRepositoryImpl : QuizRepository {
    private val TAG = "QuizRepositoryImpl"



    @SuppressLint("CheckResult")
    override fun getCountriesListRepository(): Single<CountriesResponse> {
//        CoroutineScope(Dispatchers.IO).launch {
//            ApiFactory.apiService.loadCountries()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Log.d(TAG, "fun getCountriesListRepository() .subscribe: + ${it.countries}")
//                }) {
//                    Log.d(TAG, "fun loadMovies() .subscribe exeption: + $it")
//                }
//        }
            return ApiFactory.apiService.loadCountries()

    }
    override fun getCountriesListNotUsedRepository(dbDAO: CountriesDao): LiveData<List<Country>> {
        TODO("Not yet implemented")
    }
}