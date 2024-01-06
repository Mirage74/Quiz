package com.balex.quiz.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.room.Room
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.database.CountriesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"
private const val DB_NAME = "CountriesList.db"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countriesDatabase = Room.databaseBuilder(
            application,
            CountriesDatabase::class.java,
            DB_NAME
        ).build()

        val dbDAO = countriesDatabase.countriesDao()


        CoroutineScope(Dispatchers.IO).launch {
            ApiFactory.apiService.loadCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, it.toString())
                }) {
                    Log.d(
                        TAG,
                        "fun loadMovies() .subscribe exeption: + ${it.toString()}"
                    )
                }


        }



    }
}

