package com.balex.quiz.presentation

import android.app.Application
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balex.quiz.data.NUMBER_ANSWER_OPTIONS
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.BitmapWithIndex
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
import kotlin.concurrent.Volatile


class GameCoreViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    var currentQuestionNumber = 0
    var currentScore = 0
    var isRoundInProgress = false

    private val _isImagesDownloaded = MutableLiveData(false)
    val isImagesDownloaded: LiveData<Boolean>
        get() = _isImagesDownloaded


    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _secLeftForAnswer = MutableLiveData<Int>()
    val secLeftForAnswer: LiveData<Int>
        get() = _secLeftForAnswer


    private val _currentProgressString = MutableLiveData<String>()
    val currentProgressString: LiveData<String>
        get() = _currentProgressString


    @Volatile
    private var _countOfBitmapLoaded = MutableLiveData(0)
    val countOfBitmapLoaded: LiveData<Int>
        get() = _countOfBitmapLoaded


    private val repository = QuizRepositoryImpl(application)
    val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    var gameSettings = getGameSettingsUseCase(level)


    var countriesFullList = mutableListOf<Country>()

    var questionsList = mutableListOf<Question>()

    var bitmapImagesList = mutableListOf<Bitmap>()
    private var bitmapImagesListIndex = mutableListOf<Int>()

    fun resetNewTestData() {
        _countOfBitmapLoaded.value = 0
        _isImagesDownloaded.value = false
        currentScore = 0
    }

    fun getCurrentQuestionString(): String {
        return "$currentQuestionNumber / ${gameSettings.allQuestions}"
    }

    fun getCapitalNameById(id: Int): String {
        return countriesFullList.stream().filter { it.id == id }.findFirst()
            .get().capitalName.trim()

    }

    private fun getFrameScore(timeLeft: Int): Int {
        return when (timeLeft) {
            in gameSettings.timeRestBestScoreSec..gameSettings.timeMaxSec -> {
                gameSettings.pointsMax
            }

            in gameSettings.timeRestMediumScoreSec..<gameSettings.timeRestBestScoreSec -> {
                gameSettings.pointsMedium
            }

            else -> {
                gameSettings.pointsMin
            }
        }
    }

    fun chooseAnswer(numUserAnswer: Int) {
        timer?.cancel()
        var score = 0
        var answerId = 0
        if (numUserAnswer in 1..NUMBER_ANSWER_OPTIONS) {
            answerId = questionsList[currentQuestionNumber].getOptionId(numUserAnswer)
            if (numUserAnswer == questionsList[currentQuestionNumber].rightAnswerNumOption) {
                currentScore += getFrameScore(secLeftForAnswer.value ?: 0)
                val t = 5
            }

        } else {
            throw RuntimeException("fun chooseAnswer, answer num $numUserAnswer not in range $TIME_IS_EXPIRED..$NUMBER_ANSWER_OPTIONS")
        }


    }

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
                        _countOfBitmapLoaded.value = _countOfBitmapLoaded.value?.plus(1)
                        _currentProgressString.value =
                            "" + _countOfBitmapLoaded.value + " / " + gameSettings.allQuestions
                        if (_countOfBitmapLoaded.value == figureTargetList.size) {
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


    fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.timeMaxSec * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = "${(millisUntilFinished / MILLIS_IN_SECONDS)} sec."
                _secLeftForAnswer.value = (millisUntilFinished / MILLIS_IN_SECONDS).toInt()
            }

            override fun onFinish() {
                chooseAnswer(TIME_IS_EXPIRED)
            }
        }
        timer?.start()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val TIME_IS_EXPIRED = 0
    }

}