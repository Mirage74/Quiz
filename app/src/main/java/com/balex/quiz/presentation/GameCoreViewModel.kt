package com.balex.quiz.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.BitmapWithIndex
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.usecases.GenerateQuestionUseCase
import com.balex.quiz.domain.usecases.GetGameSettingsUseCase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.stream.Collectors
import kotlin.concurrent.Volatile


class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {


    private val _isImagesDownloaded = MutableLiveData(false)
    val isImagesDownloaded: LiveData<Boolean>
        get() = _isImagesDownloaded

    private val repository = QuizRepositoryImpl(application)
    val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    var gameSettings = getGameSettingsUseCase(level)
    //val allQuestionsNumber = gameSettings.allQuestions.toString()

    private val _allQuestionsNumber = MutableLiveData<String>()
    val allQuestionsNumber: LiveData<String>
        get() = _allQuestionsNumber

    var countriesFullList = mutableListOf<Country>()
    private var questionsList = mutableListOf<Question>()

    var bitmapImagesList = mutableListOf<Bitmap>()
    private var bitmapImagesListIndex = mutableListOf<Int>()


    @Volatile
    var countOfBitmapLoaded = 0


    //val gameSetting = getGameSettings(level)

fun setAllQuestionsNumber() {
    _allQuestionsNumber.value = gameSettings.allQuestions.toString()
}

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

    private fun sortBitmapList(bitmapList: List<Bitmap>): MutableList<Bitmap> {
        val bitmapListNew = mutableListOf<Bitmap>()
        for (i in bitmapList.indices) {
            bitmapListNew.add(bitmapList[bitmapImagesListIndex[i]])
        }
        return bitmapListNew
    }

    fun downloadImagesToBitmap() {
        setIsImagesLoaded(false)
        val figureTargetList = getImagesFigureTarget()
        for (i in 0..<figureTargetList.size) {
            CoroutineScope(Dispatchers.IO).launch {
                getImagesFromBackendRX(i, figureTargetList[i])
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        bitmapImagesListIndex.add(it.index)
                        bitmapImagesList.add(it.figureTarget)
                        ++countOfBitmapLoaded
                        if (countOfBitmapLoaded == figureTargetList.size) {
                            bitmapImagesList = sortBitmapList(bitmapImagesList)
                            setIsImagesLoaded(true)
                        }
                        //Log.d("ddd", "fun getList i: size ${it.index}")
                        //Log.d("ddd", "gameSetting ${gameSetting.allQuestions}")
                    }) {
                        Log.d("GameCoreViewModel", "fun downloadImagesToBitmap exception : $it")
                        throw it
                    }
            }
        }
    }

    private fun getImagesFromBackendRX(
        index: Int,
        figureTarget: FutureTarget<Bitmap>
    ): Single<BitmapWithIndex> {
        return Single.fromCallable(Callable { getImagesFromBackend(index, figureTarget) })

    }

    private fun getImagesFromBackend(
        index: Int,
        figureTarget: FutureTarget<Bitmap>
    ): BitmapWithIndex {


        return BitmapWithIndex(index, figureTarget.get())

    }


    fun setQuestionList() {
        questionsList = generateListOfQuestions()
        Log.d("ddd", "$questionsList")
    }


    private fun setIsImagesLoaded(newValue: Boolean) {
        Log.d("ddd", "setIsImagesLoaded called, newvalue $newValue")
        _isImagesDownloaded.value = newValue

    }

    private fun generateQuestion(
        numQuestion: Int,
        countriesNotUsedInQuestion: List<Country>
    ): Question {
        return generateQuestionUseCase(
            level,
            numQuestion,
            countriesFullList,
            countriesNotUsedInQuestion
        )
    }

    private fun generateListOfQuestions(): MutableList<Question> {
        val questionsList = mutableListOf<Question>()


        var countriesNotUsedInQuestion = countriesFullList
        for (i in 1..gameSettings.allQuestions) {
            val question = generateQuestion(i, countriesNotUsedInQuestion)
            questionsList.add(question)
            countriesNotUsedInQuestion = countriesNotUsedInQuestion.stream().filter {
                it.id != question.id
            }.collect(Collectors.toList())
        }

        return questionsList
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}