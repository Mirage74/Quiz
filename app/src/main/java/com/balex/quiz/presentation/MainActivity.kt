package com.balex.quiz.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val s = (application as App).str()
        viewModel = ViewModelProvider(this, MainViewModelFactory(this.application))[MainViewModel::class.java]
        viewModel.isListCountriesFromBackendLoaded.observe(this) {
            if (!it) {
                viewModel.getCountriesListFromBackend()
            }
        }

        viewModel.initIsUserLoggedStatus()

    }

}



