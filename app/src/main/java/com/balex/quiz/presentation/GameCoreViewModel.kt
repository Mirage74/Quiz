package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.usecases.GenerateQuestionUseCase
import com.balex.quiz.domain.usecases.GetGameSettingsUseCase
import java.util.Collections

class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = QuizRepositoryImpl(application)
    val generateQuestion = GenerateQuestionUseCase(repository)
    val getGameSettings = GetGameSettingsUseCase(repository)

    var countriesFullList: List<Country> = Collections.emptyList()
    var countriesNotUsedInQuestion: List<Country> = Collections.emptyList()


    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val gameSetting = getGameSettings(level)

    var imageName = MutableLiveData<String>()


    fun generateQuestion(numQuestion: Int) : Question {
        val question = generateQuestion(level, numQuestion, countriesFullList, countriesNotUsedInQuestion)
        val country =
            countriesFullList.stream().filter { it.countryName == question.countryName }.findFirst()
                .get()
        imageName.value = country.imageName
        return generateQuestion(level, numQuestion, countriesFullList, countriesNotUsedInQuestion)
    }

    fun pr() {
        Log.d("ddd", countriesNotUsedInQuestion.toString())
        Log.d("ddd", gameSetting.toString())
        Log.d("ddd", generateQuestion(1).toString())
    }


}