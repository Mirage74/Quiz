package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.domain.entity.Level

class GameCoreModelFactory(
    private val application: Application,
    private val level: Level

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameCoreViewModel::class.java)) {
            Log.d("ModelFactory", "GameCoreModelFactory called")
            return GameCoreViewModel(application, level) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }


}