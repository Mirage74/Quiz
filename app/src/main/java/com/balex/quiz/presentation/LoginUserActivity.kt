package com.balex.quiz.presentation

import android.app.Activity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.data.api.AuthRequest
import com.balex.quiz.data.pojo.UserScore
import com.balex.quiz.databinding.LoginBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ERROR_ENTERED_USERNAME_MESSAGE =
    "Name length must contain 3-20 char and begin from letter; pass must be not empty"
const val LOGIN_FAILED = "Login failed"
const val LOGIN_SUCCESS = "Login success"

class LoginUserActivity : AppCompatActivity() {
    private val TAG = "LoginUserActivity"
    private lateinit var binding: LoginBinding
    private val compositeDisposable = CompositeDisposable()
    private var name1 = ""
    private var pass1 = ""
    private var state = false
    var minUserNameLen: Int = 0
    var maxUserNameLen: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login)
        minUserNameLen = resources.getInteger(R.integer.minUsernameLength)
        maxUserNameLen = resources.getInteger(R.integer.maxUsernameLength)

        val onClickListener =
            View.OnClickListener { v: View ->
                if (v.id == R.id.login) {
                    login()
                } else if (v.id == R.id.toggle_view1) {
                    toggle()
                }
            }

        with(binding) {
            login.setOnClickListener(onClickListener)
            toggleView1.setOnClickListener(onClickListener)
        }

    }

    fun login() {
        name1 = binding.user1.toString()
        pass1 = binding.pass1.toString()
        val checkUsername =
            name1.matches("[A-Za-z]\\w+".toRegex()) && name1.length >= minUserNameLen && name1.length <= maxUserNameLen
        if (name1.isEmpty() || pass1.isEmpty() || !checkUsername) {
            userLoginToBackend(name1, pass1)
//            Toast.makeText(
//                this,
//                ERROR_ENTERED_USERNAME_MESSAGE,
//                Toast.LENGTH_SHORT
//            ).show()
        } else {

            userLoginToBackend(name1, pass1)

//            val failed_login = Toast.makeText(this, LOGIN_FAILED, Toast.LENGTH_SHORT)
//            val success_login = Toast.makeText(this, LOGIN_SUCCESS, Toast.LENGTH_SHORT)
//            val getLoginResponse: String = postLoginUser(name1, pass1)
//            if (getLoginResponse.indexOf("CODE LOGIN_USER_02") == 1) {
//                Log.d(TAG, "User login failed : $name1")
//                failed_login.show()
//            } else {
//                userInstance = parseBackendResponse(getLoginResponse)
//                //Log.i("caps",  "user : " + userInstance);
//                success_login.show()
//                saveUser(userInstance)
//                val intent = Intent(this, LoggedUser::class.java)
//                intent.putExtra(EXTRAS_COUNTY_LIST, countryList)
//                println("login user, countryList : $countryList")
//                startActivity(intent)
//            }
        }
    }

    fun toggle() {
        if (!state) {
            with(binding) {
                pass1.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pass1.setSelection(pass1.text.length)
                toggleView1.setImageResource(R.drawable.eye)
            }
        } else {
            with(binding) {
                pass1.transformationMethod = PasswordTransformationMethod.getInstance()
                pass1.setSelection(pass1.text.length)
                toggleView1.setImageResource(R.drawable.eye_off)
            }
        }
        state = !state
    }

    private fun userLoginToBackend(login: String, password: String) {
        //val authRequest = AuthRequest("login:$login", "password:$password")
        //val authRequest = AuthRequest(login,password)
        val authRequest = "login:$login, password:$password"
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                //ApiFactory.apiService.login(authRequest)
                ApiFactory.apiService.login(authRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        saveDataUser(this@LoginUserActivity, it)
                        Log.d(TAG, "user: $it")
                    }) {
                        Log.d(TAG, "Error login: + $it")
                    })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        fun saveDataUser(activity: Activity, user: UserScore) {
            val sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(SHARED_PREFS_USERNAME, user.userName)
            editor.apply()
            Log.d("LoginUserActivity", "user saved: ${user.userName}")
        }
    }

}