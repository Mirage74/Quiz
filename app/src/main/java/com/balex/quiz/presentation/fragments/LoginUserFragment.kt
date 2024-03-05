package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.LoginBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginUserFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

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
    private var maxUserNameLen: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

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
                toggleView1.setImageResource(R.drawable.eye)
            }
        } else {
            with(binding) {
                pass1.transformationMethod =
                    android.text.method.PasswordTransformationMethod.getInstance()
                pass1.setSelection(pass1.text.length)
                toggleView1.setImageResource(R.drawable.eye_off)
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

                        if (it.toString().indexOf("CODE LOGIN_USER_02") == 0) {
                            failed_login.show()
                        } else {
                            success_login.show()
                            App.saveDataUser(it.userScore,requireActivity().application)
                            viewModel.setIsUserLogged(true)
                            launchUserLoggedTrueFragment()
                        }
                    }) {
                        Log.d(TAG, "Error login: + $it")
                        failed_login.show()
                    })
        }

    }

    private fun launchUserLoggedTrueFragment() {
        findNavController().navigate(R.id.action_loginUserFragment_to_userLoggedTrueMenu)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()
    }

}