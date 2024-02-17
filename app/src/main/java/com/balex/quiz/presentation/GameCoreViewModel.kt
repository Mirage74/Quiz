package com.balex.quiz.presentation

import android.app.Application
import android.graphics.Bitmap
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
import com.bumptech.glide.request.FutureTarget
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.stream.Collectors


class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val _isImagesDownloaded = MutableLiveData(false)
    val isImagesDownloaded: LiveData<Boolean>
        get() = _isImagesDownloaded

    private val repository = QuizRepositoryImpl(application)
    val generateQuestion = GenerateQuestionUseCase(repository)
    val getGameSettings = GetGameSettingsUseCase(repository)

    var countriesFullList = mutableListOf<Country>()
    private var questionsList = mutableListOf<Question>()
    var bitmapImagesList = mutableListOf<Bitmap>()


    private val gameSetting = getGameSettings(level)


    private val compositeDisposable = CompositeDisposable()

    private fun getImagesFigureTarget(): MutableList<FutureTarget<Bitmap>> {
        val figureTargetList = mutableListOf<FutureTarget<Bitmap>>()
        for (i in 0..<questionsList.size) {
            figureTargetList.add(
                Glide.with(application)
                    .asBitmap()
                    .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${questionsList[i].imageName}")
                    .submit()
            )
        }
        return figureTargetList
    }

    fun getList() {
        setIsImagesLoaded(false)
        CoroutineScope(Dispatchers.IO).launch {
            getCountriesRX()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    bitmapImagesList = it
                    setIsImagesLoaded(true)
                }) {
                    Log.d("ViewModel", "refreshList Exception + ${it.toString()}")
                    //throw it
                }
        }

    }

    fun getCountriesRX(): Single<MutableList<Bitmap>> {
        //Log.d("ViewModel", "fun getCountriesRX");
        return Single.fromCallable(Callable { setImagesToBitmapList() })
        //return Single.fromCallable ( Callable{ throw Exception()})
    }

    fun setImagesToBitmapList(): MutableList<Bitmap> {
        val figureTargetList = getImagesFigureTarget()
        val bitmapList = mutableListOf<Bitmap>()
        for (i in 0..<questionsList.size) {

            bitmapList.add(figureTargetList[i].get())
        }

        return bitmapList

    }


    fun setQuestionList() {
        questionsList = generateListOfQuestions()
        Log.d("ddd", "$questionsList")
    }


    fun setIsImagesLoaded(newValue: Boolean) {
        _isImagesDownloaded.value = newValue

    }

    private fun generateQuestion(
        numQuestion: Int,
        countriesNotUsedInQuestion: List<Country>
    ): Question {
        return generateQuestion(
            level,
            numQuestion,
            countriesFullList,
            countriesNotUsedInQuestion
        )
    }

    private fun generateListOfQuestions(): MutableList<Question> {
        val questionsList = mutableListOf<Question>()
        var countriesNotUsedInQuestion = countriesFullList
        for (i in 1..gameSetting.allQuestions) {
            val question = generateQuestion(i, countriesNotUsedInQuestion)
            questionsList.add(question)
            countriesNotUsedInQuestion = countriesNotUsedInQuestion.stream().filter {
                it.id != question.id
            }.collect(Collectors.toList())
        }
        return questionsList
    }

//    fun loadImageAsBitmapFromBackend(imageFileName: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//
//
//            Glide.with(application)
//                .asBitmap()
//                .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${imageFileName}")
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        //imageView.setImageBitmap(resource)
//                        bitmapNextQuestion = resource
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        // this is called when imageView is cleared on lifecycle call or for
//                        // some other reason.
//                        // if you are referencing the bitmap somewhere else too other than this imageView
//                        // clear it here as you can no longer have the bitmap
//                    }
//                })
//
//        }
//
//    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}