package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.domain.usecases.DeleteCountryFromNotUsedListRepositoryUseCase
import com.balex.quiz.domain.usecases.GetCountriesFullListRepositoryUseCase
import com.balex.quiz.domain.usecases.GetCountriesListNotUsedRepositoryUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel(application: Application, isUserLogged: Boolean) :
    AndroidViewModel(application)

{


    private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl
    private val getCountriesFullListRepositoryUseCase = GetCountriesFullListRepositoryUseCase(repository)
    private val getCountriesListNotUsedUseCase = GetCountriesListNotUsedRepositoryUseCase(repository)
    val deleteCountryFromNotUsedListRepositoryUseCase = DeleteCountryFromNotUsedListRepositoryUseCase(repository)
    private val compositeDisposable = CompositeDisposable()

    val countriesListFull_LD = getCountriesFullListRepositoryUseCase()
    val countriesListNotUsedInQuiz_LD = getCountriesListNotUsedUseCase()






    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}