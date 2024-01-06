package com.balex.quiz.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.database.CountriesDatabase
import com.balex.quiz.data.pojo.Country
import com.balex.quiz.domain.GetCountriesListRepositoryUseCase
import com.balex.quiz.domain.QuizRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application, isUserLogged: Boolean) :
    AndroidViewModel(application) {

    private val DB_NAME = "CountriesList.db"
    private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl
    private val getCountriesList = GetCountriesListRepositoryUseCase(repository)
    private val compositeDisposable = CompositeDisposable()

    val countriesDatabase = Room.databaseBuilder(
        application,
        CountriesDatabase::class.java,
        DB_NAME
    ).build()

    val dbDAO = countriesDatabase.countriesDao()


//    fun loadCountriesListFromBackend() {
//        CoroutineScope(Dispatchers.IO).launch {
//            compositeDisposable.add(
//            getCountriesList.getCountriesList()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Log.d(TAG, "fun getCountriesListRepository() .subscribe: + ${it.countries}")
//                    Log.d(TAG, "fun getCountriesListRepository() .size: + ${it.countries.size}")
//                    //saveCountriesListInROOM(it.countries)
//                    val list = it.countries
//                    CoroutineScope(Dispatchers.IO).launch {
//                        dbDAO.fillCountriesTable(list)
//                    }
//                }) {
//                    Log.d(TAG, "fun loadMovies() .subscribe exeption: + $it")
//                })
//        }
//
//    }



    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}