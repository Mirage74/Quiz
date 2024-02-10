package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.data.NOT_LOGGED_USER
import com.balex.quiz.databinding.StatusUserLoggedFalseBinding
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class UserLoggedFalseMenu : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var userScore: UserScore

    private var _binding: StatusUserLoggedFalseBinding? = null
    private val binding: StatusUserLoggedFalseBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusUserLoggedFalseBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userScore = viewModel.loadUserScore()
        viewModel.userScore.value?.let { userScore = it  }
        if (userScore.userName != NOT_LOGGED_USER) {
            viewModel.refreshUserScore(userScore)
        }

        viewModel.userScore.observe(viewLifecycleOwner) {
            if (it.userName != NOT_LOGGED_USER) {
                launchUserLoggedTrueFragment()
            }
        }


        with(binding) {
            login.setOnClickListener {
                launchLoginFragment()
            }
        }
    }

    private fun launchLoginFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_loginUserFragment)
    }

    private fun launchUserLoggedTrueFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_userLoggedTrueMenu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}