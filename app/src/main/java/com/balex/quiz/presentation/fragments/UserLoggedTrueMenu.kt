package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.databinding.StatusUserLoggedTrueBinding
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class UserLoggedTrueMenu  : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: StatusUserLoggedTrueBinding? = null
    private val binding: StatusUserLoggedTrueBinding
    get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusUserLoggedTrueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUserScorePrefs()
//        with(binding) {
//            login.setOnClickListener {
//                launchLoginFragment()
//            }
//        }
    }

//    private fun launchLoginFragment() {
//        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_loginUserFragment)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}