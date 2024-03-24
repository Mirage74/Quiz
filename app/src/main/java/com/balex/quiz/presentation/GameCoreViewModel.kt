package com.balex.quiz.presentation

import android.app.Application
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balex.quiz.data.NUMBER_ANSWER_OPTIONS
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.BitmapWithIndex
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.entity.UserAnswer
import com.balex.quiz.domain.usecases.GenerateQuestionUseCase
import com.balex.quiz.domain.usecases.GetGameSettingsUseCase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.stream.Collectors
import kotlin.concurrent.Volatile

class GameCoreViewModel(
    private val application: Application
) : ViewModel() {

    val TAG = "GameCoreViewModel"
    private val UPDATE_USER_FAILED = "Update user failed"
    private val UPDATE_USER_SUCCESS = "Update user success"

    private val LOAD_USER_INFO_FAILED = "Load user data failed"
    private val LOAD_USER_INFO_SUCCESS = "Load user data success"

    var currentQuestionNumber = MutableLiveData<Int>()

    var level = Level.EASY

    var currentScore = 0
    var isRoundInProgress = false

    private val _isImagesDownloaded = MutableLiveData(false)
    val isImagesDownloaded: LiveData<Boolean>
        get() = _isImagesDownloaded

    val isQuizFinished = MutableLiveData(false)

    var lockButtons = false


    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime


    private val secLeftForAnswer = MutableLiveData<Int>()


    private val _currentProgressString = MutableLiveData<String>()
    val currentProgressString: LiveData<String>
        get() = _currentProgressString


    val _currentQuestionString = MutableLiveData<String>()
    val currentQuestionString: LiveData<String>
        get() = _currentQuestionString


    @Volatile
    private var _countOfBitmapLoaded = MutableLiveData(0)
    val countOfBitmapLoaded: LiveData<Int>
        get() = _countOfBitmapLoaded


    private val repository = QuizRepositoryImpl(application)
    val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    var gameSettings = GameSettings.getEmptyInstance()

    var countriesFullList = mutableListOf<Country>()

    var questionsList = mutableListOf<Question>()

    var userAnswers = mutableListOf<UserAnswer>()

    var bitmapImagesList = mutableListOf<Bitmap>()
    private var bitmapImagesListIndex = mutableListOf<Int>()

    private val compositeDisposable = CompositeDisposable()

    fun setGameSettings(level: Level) {
        gameSettings = getGameSettingsUseCase(level)
    }

    fun resetNewTestData() {
        _countOfBitmapLoaded.value = 0
        _isImagesDownloaded.value = false
        currentScore = 0
    }

//    fun getCurrentQuestionString(): String {
//        return "${currentQuestionNumber.value} / ${gameSettings.allQuestions}"
//    }

    fun getCapitalNameById(id: Int): String {
        if (id > 0 && id <= countriesFullList.size) {
            return countriesFullList.stream().filter { it.id == id }.findFirst()
                .get().capitalName.trim()

        } else {
            throw RuntimeException("fun getCapitalNameById error, id = $id, but id must be between 1 and ${countriesFullList.size}")
        }


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
        var scoreFrame = 0
        var answerId = 0
        var currQuestionNotNull = 0
        currentQuestionNumber.value?.let {
            currQuestionNotNull = it
        }
        if (currQuestionNotNull > 0) {

            if (!lockButtons) {


                if (numUserAnswer in TIME_IS_EXPIRED..NUMBER_ANSWER_OPTIONS) {
                    var secRest = 0
                    secLeftForAnswer.value?.let { secRest = it}
                    if (secRest > TIME_IS_EXPIRED) {
                        answerId = questionsList[currQuestionNotNull - 1].getOptionId(numUserAnswer)
                        if (questionsList[currQuestionNotNull - 1].isAnswerCorrect(numUserAnswer)) {
                            scoreFrame = getFrameScore(secLeftForAnswer.value ?: 0)
                            currentScore += scoreFrame

                        }

                    }
                    userAnswers.add(
                        UserAnswer(
                            currQuestionNotNull,
                            questionsList[currQuestionNotNull - 1].id,
                            answerId,
                            scoreFrame
                        )
                    )


                    currQuestionNotNull++
                    if (currQuestionNotNull <= gameSettings.allQuestions) {
                        currentQuestionNumber.value = currQuestionNotNull
                        _currentQuestionString.value =
                            "${currentQuestionNumber.value} / ${gameSettings.allQuestions}"
                        startTimer()
                    } else {
                        timer?.cancel()
                        lockButtons = true
                        updateUserBackend()
                    }


                } else {
                    throw RuntimeException("fun chooseAnswer, answer num $numUserAnswer not in range $TIME_IS_EXPIRED..$NUMBER_ANSWER_OPTIONS")
                }
            }
        } else {
            throw RuntimeException("fun chooseAnswer, currQuestionNotNull  $currQuestionNotNull = 0")
        }
    }

    private fun updateUserBackend() {
        val failed_update = Toast.makeText(application, UPDATE_USER_FAILED, Toast.LENGTH_SHORT)
        val success_update = Toast.makeText(application, UPDATE_USER_SUCCESS, Toast.LENGTH_SHORT)
        val userName = App.loadUserNameFromPrefsCapitalized(application).trim()
        CoroutineScope(Dispatchers.IO).launch {
            //Log.d(TAG, "UserAnswer.serializeListOfInstances(userAnswers): ${UserAnswer.serializeListOfInstances(userAnswers)}")
            compositeDisposable.add(

                ApiFactory.apiService.updateUser(
                    userName,
                    currentScore.toString(),
                    UserAnswer.serializeListOfInstances(userAnswers)
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.indexOf("UPDATE_USER_01") == 0) {
                            success_update.show()
                            getUserScoreFromBackend(userName)
                        } else {
                            failed_update.show()
                        }
                    }) {
                        Log.d(TAG, "Error update user: + $it")
                        failed_update.show()
                    })
        }

    }

    private fun getUserScoreFromBackend(userName: String) {
        val failed_load_user =
            Toast.makeText(application, LOAD_USER_INFO_FAILED, Toast.LENGTH_SHORT)
        val success_load_user =
            Toast.makeText(application, LOAD_USER_INFO_SUCCESS, Toast.LENGTH_SHORT)
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.getUserScore(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.userScore.toString().indexOf("userName=") >= 0) {
                            success_load_user.show()
                            App.saveDataUser(it.userScore, application)
                            isQuizFinished.value = true
                        } else {
                            failed_load_user.show()
                        }
                    }) {
                        Log.d(TAG, "Error get user data: + $it")
                        failed_load_user.show()
                    })
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
        return Single.fromCallable { getImagesFromBackend(index, figureTarget) }

    }

    private fun getImagesFromBackend(
        index: Int,
        figureTarget: FutureTarget<Bitmap>
    ): BitmapWithIndex {


        return BitmapWithIndex(index, figureTarget.get())

    }


    fun setQuestionList() {
        questionsList = generateListOfQuestions()

    }


    fun setIsImagesLoaded(newValue: Boolean) {
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
                secLeftForAnswer.value = (millisUntilFinished / MILLIS_IN_SECONDS).toInt()
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }
}