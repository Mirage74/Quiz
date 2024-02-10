package com.balex.quiz.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.balex.quiz.data.NOT_LOGGED_USER
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.SHARED_PREFS
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_POINTS
import com.balex.quiz.data.SHARED_PREFS_LAST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_USERNAME
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.domain.usecases.GetCountriesFullListRepositoryUseCase
import com.balex.quiz.domain.usecases.GetUserScoreUseCase
import com.balex.quiz.domain.usecases.SetUserScoreUseCase

class MainViewModel(application: Application) :
    AndroidViewModel(application) {


    val applicationViewModel = application

    private val TAG = "MainViewModel"



    private val repository = QuizRepositoryImpl(application)



    private val countriesFullListLiveData = GetCountriesFullListRepositoryUseCase(repository)
    private val getUserScore = GetUserScoreUseCase(repository)
    private val setUserScore = SetUserScoreUseCase(repository)

    val userScore = getUserScore()

    val notLogUserScoreInstance = UserScore(NOT_LOGGED_USER, 0, "", "")

    private fun loadUserNameFromPrefs(): String {
        val sharedPreferences = applicationViewModel.getSharedPreferences(
            SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        return sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER).toString()
    }


     fun loadUserScore(): UserScore {
        val userName = loadUserNameFromPrefs()
        return if (userName == NOT_LOGGED_USER) {
            notLogUserScoreInstance
        } else {
            val sharedPreferences = applicationViewModel.getSharedPreferences(
                SHARED_PREFS,
                AppCompatActivity.MODE_PRIVATE
            )
            UserScore(
                userName,
                sharedPreferences.getInt(SHARED_PREFS_BEST_RES_POINTS, 0),
                sharedPreferences.getString(SHARED_PREFS_BEST_RES_CONTENT, "").toString(),
                sharedPreferences.getString(SHARED_PREFS_LAST_RES_CONTENT, "").toString()
            )
        }
    }

    fun refreshUserScore(userScore: UserScore) {
        setUserScore(userScore)
    }

    fun refreshAndSaveUserScore(userScore: UserScore) {
        setUserScore(userScore)
        App.saveDataUser(userScore, getApplication())
    }

    fun setUserScoreAsNotLogged() {
        setUserScore(notLogUserScoreInstance)
        App.saveDataUser(notLogUserScoreInstance, getApplication())
    }



}