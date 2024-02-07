package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.balex.quiz.R
import com.balex.quiz.data.SHARED_PREFS
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_POINTS
import com.balex.quiz.data.SHARED_PREFS_LAST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_USERNAME
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.LoginBinding
import com.balex.quiz.domain.entity.UserScore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginUserFragment : Fragment() {

    private val ERROR_ENTERED_USERNAME_MESSAGE =
        "Name length must contain 3-20 char and begin from letter; pass must be not empty"
    private val LOGIN_FAILED = "Login failed"
    private val LOGIN_SUCCESS = "Login success"

    private var _binding: LoginBinding? = null
    private val binding: LoginBinding
        get() = _binding ?: throw RuntimeException("LoginUserFragment == null")
    val TAG = "LoginUserFragment"

    private val compositeDisposable = CompositeDisposable()
    private var nameText = ""
    private var passText = ""
    private var state = false
    private var minUserNameLen: Int = 0
    var maxUserNameLen: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
//            buttonLevelTest.setOnClickListener {
//                launchGameFragment(Level.TEST)
//            }

        }
    }


    fun login() {
        nameText = binding.user1.text.toString()
        passText = binding.pass1.text.toString()
        val checkUsername =
            nameText.matches("[A-Za-z]\\w+".toRegex()) && nameText.length >= minUserNameLen && nameText.length <= maxUserNameLen
        if (nameText.isEmpty() || passText.isEmpty() || !checkUsername) {
            Toast.makeText(
                requireActivity(),
                ERROR_ENTERED_USERNAME_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
        } else {

            userLoginToBackend(nameText, passText)


        }
    }

    fun toggle() {
        if (!state) {
            with(binding) {
                pass1.transformationMethod =
                    android.text.method.HideReturnsTransformationMethod.getInstance()
                pass1.setSelection(pass1.text.length)
                toggleView1.setImageResource(com.balex.quiz.R.drawable.eye)
            }
        } else {
            with(binding) {
                pass1.transformationMethod =
                    android.text.method.PasswordTransformationMethod.getInstance()
                pass1.setSelection(pass1.text.length)
                toggleView1.setImageResource(com.balex.quiz.R.drawable.eye_off)
            }
        }
        state = !state
    }

    private fun userLoginToBackend(login: String, password: String) {

        val failed_login = Toast.makeText(requireActivity(), LOGIN_FAILED, Toast.LENGTH_SHORT)
        val success_login = Toast.makeText(requireActivity(), LOGIN_SUCCESS, Toast.LENGTH_SHORT)
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                //ApiFactory.apiService.login(authRequest)
                ApiFactory.apiService.login(login, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        if (it.toString().indexOf("CODE LOGIN_USER_02") == 1) {
                            failed_login.show()
                        } else {
                            success_login.show()
                            saveDataUser(it.userScore)
                        }
                    }) {
                        Log.d(TAG, "Error login: + $it")
                        failed_login.show()
                    })
        }

    }


    private fun saveDataUser(user: UserScore) {
        val sharedPreferences = requireActivity().getSharedPreferences(
            SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFS_USERNAME, user.userName)
        editor.putInt(SHARED_PREFS_BEST_RES_POINTS, user.bestScore)
        editor.putString(SHARED_PREFS_BEST_RES_CONTENT, user.bestResultJSON)
        editor.putString(SHARED_PREFS_LAST_RES_CONTENT, user.lastResultJSON)
        editor.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()
    }

}