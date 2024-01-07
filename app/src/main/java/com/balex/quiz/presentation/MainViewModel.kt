package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.database.CountriesDatabase
import com.balex.quiz.data.pojo.Country
import com.balex.quiz.domain.DeleteCountryFromNotUsedListRepositoryUseCase
import com.balex.quiz.domain.GetCountriesFullListRepositoryUseCase
import com.balex.quiz.domain.GetCountriesListNotUsedRepositoryUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel(application: Application, isUserLogged: Boolean) :
    AndroidViewModel(application) {

    private val DB_NAME = "CountriesList.db"
    private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl
    private val getCountriesListFullUseCase = GetCountriesFullListRepositoryUseCase(repository)
    private val getCountriesListNotUsedUseCase = GetCountriesListNotUsedRepositoryUseCase(repository)
    val deleteCountryFromNotUsedListRepositoryUseCase = DeleteCountryFromNotUsedListRepositoryUseCase(repository)
    private val compositeDisposable = CompositeDisposable()

    val countriesListFull = getCountriesListFullUseCase.getCountriesFullList()
    val countriesListNotUsed = getCountriesListNotUsedUseCase.getCountriesListNotUsed()


    val countriesDatabase = Room.databaseBuilder(
        application,
        CountriesDatabase::class.java,
        DB_NAME
    ).build()

    val dbDAO = countriesDatabase.countriesDao()




    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}