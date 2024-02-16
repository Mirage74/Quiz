package com.balex.quiz.presentation

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.usecases.GenerateQuestionUseCase
import com.balex.quiz.domain.usecases.GetGameSettingsUseCase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.stream.Collectors


class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = QuizRepositoryImpl(application)
    val generateQuestion = GenerateQuestionUseCase(repository)
    val getGameSettings = GetGameSettingsUseCase(repository)

    var countriesFullList: List<Country> = Collections.emptyList()
    var countriesNotUsedInQuestion: List<Country> = Collections.emptyList()

    var bitmapNextQuestion: Bitmap? = null

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val gameSetting = getGameSettings(level)

    var imageName = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    fun generateQuestion(numQuestion: Int): Question {
        val question =
            generateQuestion(level, numQuestion, countriesFullList, countriesNotUsedInQuestion)
        val country =
            countriesFullList.stream().filter { it.countryName == question.countryName }.findFirst()
                .get()
        imageName.value = country.imageName
        return generateQuestion(level, numQuestion, countriesFullList, countriesNotUsedInQuestion)
    }

    fun loadImageAsBitmapFromBackend(imageFileName: String) {
        CoroutineScope(Dispatchers.IO).launch {


            Glide.with(application)
                .asBitmap()
                .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${imageFileName}")
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        //imageView.setImageBitmap(resource)
                        bitmapNextQuestion = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })

        }

    }

    fun pr() {
        Log.d("ddd", countriesNotUsedInQuestion.toString())
        Log.d("ddd", gameSetting.toString())
        Log.d("ddd", generateQuestion(1).toString())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}