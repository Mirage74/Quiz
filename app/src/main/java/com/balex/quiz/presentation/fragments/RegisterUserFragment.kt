package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.RegisterBinding
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.presentation.QuizApp
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterUserFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val ERROR_ENTERED_USERNAME_MESSAGE =
        "Name length must contain 3-20 char and begin from letter; pass must be not empty"
    private val REGISTER_FAILED = "Register failed"
    private val REGISTER_SUCCESS = "Register success"

    private var _binding: RegisterBinding? = null
    private val binding: RegisterBinding
        get() = _binding ?: throw RuntimeException("LoginUserFragment == null")
    //val TAG = "RegisterUserFragment"

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
        _binding = RegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

        minUserNameLen = resources.getInteger(R.integer.minUsernameLength)
        maxUserNameLen = resources.getInteger(R.integer.maxUsernameLength)

        val onClickListener =
            View.OnClickListener { v: View ->
                if (v.id == R.id.register) {
                    register()
                } else if (v.id == R.id.toggle_view2) {
                    toggle()
                }
            }

        with(binding) {
            register.setOnClickListener(onClickListener)
            toggleView2.setOnClickListener(onClickListener)
        }

        return binding.root
    }



    fun register() {
        nameText = binding.user2.text.toString()
        passText = binding.pass2.text.toString()
        val checkUsername =
            nameText.matches("[A-Za-z]\\w+".toRegex()) && nameText.length >= minUserNameLen && nameText.length <= maxUserNameLen
        if (nameText.isEmpty() || passText.isEmpty() || !checkUsername) {
            Toast.makeText(
                requireActivity(),
                ERROR_ENTERED_USERNAME_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
        } else {

            userRegisterToBackend(nameText, passText)


        }
    }

    private fun toggle() {
        if (!state) {
            with(binding) {
                pass2.transformationMethod =
                    android.text.method.HideReturnsTransformationMethod.getInstance()
                pass2.setSelection(pass2.text.length)
                toggleView2.setImageResource(R.drawable.eye)
            }
        } else {
            with(binding) {
                pass2.transformationMethod =
                    android.text.method.PasswordTransformationMethod.getInstance()
                pass2.setSelection(pass2.text.length)
                toggleView2.setImageResource(R.drawable.eye_off)
            }
        }
        state = !state
    }

    private fun userRegisterToBackend(login: String, password: String) {

        val failed_register = Toast.makeText(requireActivity(), REGISTER_FAILED, Toast.LENGTH_SHORT)
        val success_register = Toast.makeText(requireActivity(), REGISTER_SUCCESS, Toast.LENGTH_SHORT)
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.registerUser(login.trim(), password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.indexOf("CODE REG_USER_01") == 0) {
                            success_register.show()
                            QuizApp.saveDataUser(UserScore.getEmptyInstanceWithUserName(login.trim()), requireActivity())
                            viewModel.setIsUserLogged(true)
                            launchUserLoggedTrueFragment()
                        } else {
                            failed_register.show()
                        }
                    }) {
                        //Log.d(TAG, "Error register user: + $it")
                        failed_register.show()
                    })
        }

    }

    private fun launchUserLoggedTrueFragment() {
        findNavController().navigate(R.id.action_registerUserFragment_to_userLoggedTrueMenu)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.dispose()
    }
}