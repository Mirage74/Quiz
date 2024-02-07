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




//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var isListFromBackendLoaded = false
    var isUserLogged = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        assignButtonWithClickListeners()

        val userName = loadDataUser(this)




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

                finish()
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





}



