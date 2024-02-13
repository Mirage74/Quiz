package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.NOT_LOGGED_USER
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.UserScore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class MainViewModel(application: Application) :
    AndroidViewModel(application) {

    init {
//        getCountriesListFromBackend()
    }

    private val TAG = "MainViewModel"

    private val repository = QuizRepositoryImpl(application)
    private val compositeDisposable = CompositeDisposable()

    private val _countriesListFullLiveData = MutableLiveData<List<Country>>()
    val countriesListFullLiveData: LiveData<List<Country>>
        get() = _countriesListFullLiveData

    private val _userScore = MutableLiveData<UserScore>()
    val userScore: LiveData<UserScore>
        get() = _userScore


    val notLogUserScoreInstance = UserScore(NOT_LOGGED_USER, 0, "", "")

    private fun setСountriesListFullLiveData(list: List<Country>) {
        _countriesListFullLiveData.value = list
    }

    fun getCountriesListFromBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.loadCountries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setСountriesListFullLiveData(
                            it.countries.stream().sorted { o1, o2 -> o1.id.compareTo(o2.id) }
                                .collect(
                                    Collectors.toList()
                                )
                        )

                    }) {
                        throw (RuntimeException("Error get countries list from server: + $it"))
                    })
        }

    }

    fun setUserScore(newUserScore: UserScore) {
        _userScore.value = newUserScore
    }


    fun setAndSaveUserScore(userScore: UserScore) {
        setUserScore(userScore)
        App.saveDataUser(userScore)
    }

    fun setAndSaveUserScoreAsNotLogged() {
        setUserScore(notLogUserScoreInstance)
        App.saveDataUser(notLogUserScoreInstance)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}