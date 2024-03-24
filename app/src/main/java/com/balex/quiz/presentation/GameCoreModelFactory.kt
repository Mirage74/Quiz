package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameCoreModelFactory(
    private val application: Application

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameCoreViewModel::class.java)) {
            Log.d("ModelFactory", "GameCoreModelFactory called")
            return GameCoreViewModel(application) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }


}