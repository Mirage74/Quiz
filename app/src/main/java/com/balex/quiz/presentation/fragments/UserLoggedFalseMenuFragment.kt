package com.balex.quiz.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.databinding.StatusUserLoggedFalseBinding
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.QuizApp
import com.balex.quiz.presentation.ViewModelFactory
import javax.inject.Inject

class UserLoggedFalseMenuFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as QuizApp).component
    }

    private var _binding: StatusUserLoggedFalseBinding? = null
    private val binding: StatusUserLoggedFalseBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusUserLoggedFalseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.isUserLogged.observe(viewLifecycleOwner) {
            if (it) {
                launchUserLoggedTrueFragment()
            }
        }
        with(binding) {
            login.setOnClickListener {
                launchLoginFragment()
            }

            register.setOnClickListener {
                launchRegisterFragment()
            }

            testRules.setOnClickListener {
                launchQuizRulesFragment()
            }

            about.setOnClickListener{
                launchAboutFragment()
            }
        }
    }

    private fun launchLoginFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_loginUserFragment)
    }

    private fun launchRegisterFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_registerUserFragment)
    }

    private fun launchUserLoggedTrueFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_userLoggedTrueMenu)
    }

    private fun launchAboutFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_aboutFragment)
    }

    private fun launchQuizRulesFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_gameRulesFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}