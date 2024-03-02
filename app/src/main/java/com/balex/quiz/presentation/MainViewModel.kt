package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.entity.UserAnswer
import com.balex.quiz.domain.entity.UserScore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.stream.Collectors

class MainViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _isListCountriesFromBackendLoaded = MutableLiveData(false)
    val isListCountriesFromBackendLoaded: LiveData<Boolean>
        get() = _isListCountriesFromBackendLoaded

    private val _isUserLogged = MutableLiveData(false)
    val isUserLogged: LiveData<Boolean>
        get() = _isUserLogged


    private val compositeDisposable = CompositeDisposable()

    var countriesFullList= mutableListOf<Country>()
        set(list) {
            field = list
            _isListCountriesFromBackendLoaded.value = true
        }


//    private fun setCountriesFullList(list: List<Country>) {
//        countriesFullList = list
//        _isListCountriesFromBackendLoaded.value = true
//    }

    fun initIsUserLoggedStatus() {
        if (App.loadUserNameFromPrefs(getApplication()) != NOT_LOGGED_USER) {
            setIsUserLogged(true)
        } else {
            setIsUserLogged(false)
        }
    }

    fun setIsUserLogged(newStatus: Boolean) {
        _isUserLogged.value = newStatus
    }

    fun getCountriesListFromBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.loadCountries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        countriesFullList =
                            it.countries.stream().sorted { o1, o2 -> o1.id.compareTo(o2.id) }
                                .collect(
                                    Collectors.toList()
                                )


                        val arrAnswers = mutableListOf<UserAnswer>()
                        val userScore = App.loadUserScore(getApplication())
                        val serializedScore = userScore.serializeInstance()
                        val newUserScore = UserScore.deserializeInstance(serializedScore)
                        var oneRes = UserAnswer(1, 4 ,4, 7)
                        arrAnswers.add(oneRes)
                        oneRes = UserAnswer(2, 11 ,11, 4)
                        arrAnswers.add(oneRes)
                        oneRes = UserAnswer(3, 33 ,10, 0)
                        arrAnswers.add(oneRes)
                        val serializedArrAnswers = UserAnswer.serializeListOfInstances(arrAnswers)
                        val arrayQuestions = UserAnswer.deserializeListOfInstances(serializedArrAnswers)

//        Log.d(TAG, "userScore: $userScore")
//        Log.d(TAG, "serializedScore: $serializedScore")
                        Log.d(TAG, "newUserScore.bestResultJSON: ${newUserScore.bestResultJSON}")
                        Log.d(TAG, "serializedArrAnswers: $serializedArrAnswers")
//        Log.d(TAG, "userScore == newUserScore: ${userScore == newUserScore}")
                        Log.d(TAG, "arrayQuestions: $arrayQuestions")
                        Log.d(TAG, "oneRes: ${oneRes.getCapitalNameRightAnswer(it.countries)}")
                        Log.d(TAG, "oneRes: ${oneRes.getCapitalNameUserAnswer(it.countries)}")
                        Log.d(TAG, "oneRes: ${oneRes.getCountryName(it.countries)}")



                    }) {
                        throw (RuntimeException("Error get countries list from server: + $it"))
                    })
        }

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}