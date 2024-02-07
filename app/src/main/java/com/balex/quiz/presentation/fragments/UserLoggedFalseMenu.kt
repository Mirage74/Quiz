package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.databinding.StatusUserLoggedFalseBinding

class UserLoggedFalseMenu : Fragment() {

    private var _binding: StatusUserLoggedFalseBinding? = null
    private val binding: StatusUserLoggedFalseBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusUserLoggedFalseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            login.setOnClickListener {
                launchLoginFragment()
            }
        }
    }

    private fun launchLoginFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_loginUserFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}