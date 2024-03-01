package com.balex.quiz.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import com.balex.quiz.domain.entity.UserScore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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

        val arS = mutableListOf<UserScore>()

        val us = App.loadUserScore(this)
        val ooo = us.getJsonInstance()
        val o2 = us.getJsonInstance()
        arS.add(us)
        arS.add(us)
        val eee = Gson().toJson(arS)

        val gson = Gson()
        val itemType = object : TypeToken<List<UserScore>>() {}.type
        val itemList = gson.fromJson<List<UserScore>>(eee, itemType)


        val yyy = 6





    }


}



