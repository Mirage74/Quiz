package com.balex.quiz.presentation

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.database.CountriesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val SHARED_PREFS = "shared_prefs"
const val SHARED_PREFS_USERNAME = "shared_prefs_username"
const val NOT_LOGGED_USER = "notLoggedUser"


private const val LOGGED_USER_FALSE = false
private const val LOGGED_USER_TRUE = true


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUserLogged = if (loadDataUser(this) == NOT_LOGGED_USER) {
            LOGGED_USER_FALSE
        } else {
            LOGGED_USER_TRUE
        }

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(application, isUserLogged))[MainViewModel::class.java]

        viewModel.countriesListNotUsed.observe(this) {
            Log.d(TAG, it[0].toString())
            if (it[0].id < 3) {
                viewModel.deleteCountryFromNotUsedListRepositoryUseCase.deleteCountryFromNotUsedList(it[0])
            }

        }
    }

    companion object {
        fun loadDataUser(activity: Activity) : String {
            val sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            return sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER).toString()
        }
    }

}



