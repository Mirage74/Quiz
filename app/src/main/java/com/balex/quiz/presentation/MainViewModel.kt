package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.domain.usecases.GetCountriesFullListRepositoryUseCase
import com.balex.quiz.domain.usecases.GetUserScoreUseCase
import com.balex.quiz.domain.usecases.LoadUserScorePrefsUseCase
import com.balex.quiz.domain.usecases.RefreshUserScorePrefsUseCase

class MainViewModel(application: Application) :
    AndroidViewModel(application) {

    init {
        Log.d("MainViewModel", "MainViewModel Init called")
    }


    //private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl(application)

    private val getCountriesFullListLiveData =
        GetCountriesFullListRepositoryUseCase(repository)
    private val getUserScoreLiveData = GetUserScoreUseCase(repository)
    private val refreshUserScore = RefreshUserScorePrefsUseCase(repository)
    private val loadUserScore = LoadUserScorePrefsUseCase(repository)


    val countriesFullListLiveData = getCountriesFullListLiveData
    val userScoreLiveData = getUserScoreLiveData()
    val refreshUserScoreLiveData = refreshUserScore


    fun loadUserScorePrefs() {
        refreshUserScore(loadUserScore())
    }


}