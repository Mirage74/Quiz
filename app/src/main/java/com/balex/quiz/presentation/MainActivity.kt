package com.balex .quiz.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.databinding.ActivityMainBinding
import com.balex.quiz.presentation.LoginUserActivity.Companion.saveDataUser


const val SHARED_PREFS = "shared_prefs"
const val SHARED_PREFS_USERNAME = "shared_prefs_username"
const val SHARED_PREFS_BEST_RES_POINTS = "shared_prefs_best_res_points"
const val SHARED_PREFS_BEST_RES_CONTENT = "shared_prefs_best_res_content"
const val SHARED_PREFS_LAST_RES_CONTENT = "shared_prefs_last_res_content"
const val NOT_LOGGED_USER = "notLoggedUser"


private const val USER_IS_LOGGED = true


//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isListFromBackendLoaded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        assignButtonWithClickListeners()

        val userName = loadDataUser(this)
        val isUserLogged = if (userName == NOT_LOGGED_USER) {
            with (binding) {
                username?.visibility = View.GONE
                startTest.visibility = View.GONE
                info.visibility = View.GONE
                logout.visibility = View.GONE
            }
            !USER_IS_LOGGED
        } else {
            with (binding) {
                login.visibility = View.GONE
                register.visibility = View.GONE
                testRules.visibility = View.GONE
                username?.text = userName
                startTest.isEnabled= false
            }
            USER_IS_LOGGED
        }



        viewModel =
            ViewModelProvider(this, MainViewModelFactory(application, isUserLogged))[MainViewModel::class.java]



        viewModel.countriesListFull_LD.observe(this) {
            if (it.isNotEmpty()) {
                binding.startTest.isEnabled = true
            }
            //Log.d(TAG, it[0].toString())

        }

        viewModel.countriesListNotUsedInQuiz_LD.observe(this) {
            Log.d(TAG, it[0].toString())

        }
    }

    var onClickListener =
        View.OnClickListener { v: View ->
            if (v.id == R.id.login) {
                val intent = Intent(this@MainActivity, LoginUserActivity::class.java)
                startActivity(intent)
            } else if (v.id == R.id.register) {

            } else if (v.id == R.id.testRules) {

            } else if (v.id == R.id.about) {

            } else if (v.id == R.id.start_test) {

            } else if (v.id == R.id.info) {

            } else if (v.id == R.id.logout) {
                val userClear = UserScore(NOT_LOGGED_USER, 0, "", "")
                saveDataUser(this@MainActivity, userClear)
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }



    fun assignButtonWithClickListeners() {
        with(binding) {
            login.setOnClickListener(onClickListener)
            register.setOnClickListener(onClickListener)
            testRules.setOnClickListener(onClickListener)
            about.setOnClickListener(onClickListener)
            startTest.setOnClickListener(onClickListener)
            info.setOnClickListener(onClickListener)
            logout.setOnClickListener(onClickListener)
        }
    }




    fun setupClickListeners(buttonID: Int) {
        with(binding) {
            if (buttonID == R.id.login) {
                val intent = Intent(this@MainActivity, LoginUserActivity::class.java)
                startActivity(intent)
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



