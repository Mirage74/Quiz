package com.balex.quiz.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import com.balex.quiz.databinding.ActivityMainBinding


const val SHARED_PREFS = "shared_prefs"
const val SHARED_PREFS_USERNAME = "shared_prefs_username"
const val NOT_LOGGED_USER = "notLoggedUser"


private const val LOGGED_USER_FALSE = false
private const val LOGGED_USER_TRUE = true


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val isUserLogged = if (loadDataUser(this) == NOT_LOGGED_USER) {
            with (binding) {
                username?.visibility = View.GONE
                startTest.visibility = View.GONE
                info.visibility = View.GONE
                logout.visibility = View.GONE
            }
            LOGGED_USER_FALSE
        } else {
            with (binding) {
                login.visibility = View.GONE
                register.visibility = View.GONE
                testRules.visibility = View.GONE
            }
            LOGGED_USER_TRUE
        }
        val intent = Intent(this, LoginUserActivity::class.java)
        startActivity(intent)

        viewModel =
            ViewModelProvider(this, MainViewModelFactory(application, isUserLogged))[MainViewModel::class.java]

        viewModel.countriesListNotUsed.observe(this) {
            //Log.d(TAG, it[0].toString())

        }
    }

    companion object {
        fun loadDataUser(activity: Activity) : String {
            val sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            return sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER).toString()
        }
    }

}



