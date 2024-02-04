package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.domain.DeleteCountryFromNotUsedListRepositoryUseCase
import com.balex.quiz.domain.GetCountriesFullListRepositoryUseCase
import com.balex.quiz.domain.GetCountriesListNotUsedRepositoryUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel(application: Application, isUserLogged: Boolean) :
    AndroidViewModel(application)

{


    private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl
    private val getCountriesListFullUseCase = GetCountriesFullListRepositoryUseCase(repository)
    private val getCountriesListNotUsedUseCase = GetCountriesListNotUsedRepositoryUseCase(repository)
    val deleteCountryFromNotUsedListRepositoryUseCase = DeleteCountryFromNotUsedListRepositoryUseCase(repository)
    private val compositeDisposable = CompositeDisposable()

    val countriesListFull_LD = getCountriesListFullUseCase.getCountriesFullList()
    val countriesListNotUsedInQuiz_LD = getCountriesListNotUsedUseCase.getCountriesListNotUsed()






    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}