package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.balex.quiz.domain.entity.Level

class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {
    fun getArg(): String {
        return level.toString()
    }
}