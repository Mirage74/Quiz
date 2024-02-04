package com.balex.quiz.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.pojo.Country
import com.balex.quiz.domain.QuizRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.stream.Collectors

object QuizRepositoryImpl : QuizRepository {
    init {
        getCountriesListFromBackend()
    }


    private val countriesListFull_LD = MutableLiveData<List<Country>>()
    private val countriesListNotUsedInQuiz_LD = MutableLiveData<List<Country>>()
    private val compositeDisposable = CompositeDisposable()
    private const val TAG = "QuizRepositoryImpl"


    private fun getCountriesListFromBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.loadCountries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        countriesListFull_LD.value =
                            it.countries.stream().sorted { o1, o2 -> o1.id.compareTo(o2.id) }
                                .collect(
                                    Collectors.toList()
                                )
                        countriesListNotUsedInQuiz_LD.value = countriesListFull_LD.value
                    }) {
                        Log.d(TAG, "Error get countries list from server: + $it")
                    })
        }

    }


    override fun getCountriesListFullRepository(): LiveData<List<Country>> {
        return countriesListFull_LD
    }

    override fun getCountriesListNotUsedRepository(): LiveData<List<Country>> {
        return countriesListNotUsedInQuiz_LD
    }

    override fun deleteCountryFromNotUsedListRepository(country: Country) {
        countriesListNotUsedInQuiz_LD.value =
            countriesListNotUsedInQuiz_LD.value?.stream()?.filter { e ->
                e != country
            }?.collect(Collectors.toList())

    }
}