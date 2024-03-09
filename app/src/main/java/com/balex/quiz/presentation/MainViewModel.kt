package com.balex.quiz.presentation

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.UserAnswer
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

    private val LOAD_USER_INFO_FAILED = "Load user data failed"
    private val LOAD_USER_INFO_SUCCESS = "Load user data success"

    private val _isListCountriesFromBackendLoaded = MutableLiveData(false)
    val isListCountriesFromBackendLoaded: LiveData<Boolean>
        get() = _isListCountriesFromBackendLoaded

    private val _isUserLogged = MutableLiveData(false)
    val isUserLogged: LiveData<Boolean>
        get() = _isUserLogged

    var currentResultItemInView = MutableLiveData<UserAnswer>()


    private val compositeDisposable = CompositeDisposable()

    var countriesFullList = mutableListOf<Country>()
        set(list) {
            field = list
            _isListCountriesFromBackendLoaded.value = true
        }


    fun initIsUserLoggedStatus() {
        val userName = App.loadUserNameFromPrefsCapitalized(getApplication()).trim()
        if (userName != NOT_LOGGED_USER) {
            _isUserLogged.value?.let {
                if (!it) {
                    getUserScoreFromBackend(userName)
                    setIsUserLogged(true)
                }
            }
        } else {
            setIsUserLogged(false)
        }
    }

    private fun getUserScoreFromBackend(userName: String) {
        val failed_load_user =
            Toast.makeText(getApplication(), LOAD_USER_INFO_FAILED, Toast.LENGTH_SHORT)
        val success_load_user =
            Toast.makeText(getApplication(), LOAD_USER_INFO_SUCCESS, Toast.LENGTH_SHORT)
        Log.d("USERDATA", "getUserScoreFromBackend")
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.getUserScore(userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.toString().indexOf("userName") >= 0) {
                            success_load_user.show()
                            App.saveDataUser(it.userScore, getApplication())
                        } else {
                            failed_load_user.show()
                        }
                    }) {
                        Log.d(TAG, "Error get user data: + $it")
                        failed_load_user.show()
                    })
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