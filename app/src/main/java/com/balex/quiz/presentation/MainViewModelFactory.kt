package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory (val application: Application):
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Log.d("MainViewModelFactory", "MainViewModelFactory called")
        return MainViewModel(application) as T
    }
}