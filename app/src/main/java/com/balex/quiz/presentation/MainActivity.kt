package com.balex.quiz.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import com.balex.quiz.domain.entity.UserAnswer
import com.balex.quiz.domain.entity.UserScore
import com.google.gson.Gson

val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this.application))[MainViewModel::class.java]
        viewModel.isListCountriesFromBackendLoaded.observe(this) {
            if (!it) {
                viewModel.getCountriesListFromBackend()
            }
        }

        viewModel.initIsUserLoggedStatus()



    }


}



