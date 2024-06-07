package com.balex.quiz.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as QuizApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.isListCountriesFromBackendLoaded.observe(this) {
            if (!it) {
                viewModel.getCountriesListFromBackend()
            }
        }

        viewModel.initIsUserLoggedStatus()

    }

}



