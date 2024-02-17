package com.balex.quiz.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Question
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