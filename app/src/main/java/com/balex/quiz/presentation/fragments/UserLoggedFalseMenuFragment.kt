package com.balex.quiz.presentation.fragments

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
import com.balex.quiz.presentation.MainViewModelFactory

class UserLoggedFalseMenuFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

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


        viewModel.isUserLogged.observe(viewLifecycleOwner) {
            if (it) {
                launchUserLoggedTrueFragment()
            }
        }


        with(binding) {
            login.setOnClickListener {
                launchLoginFragment()
            }
            about.setOnClickListener{
                launchAboutFragment()
            }
        }
    }

    private fun launchLoginFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_loginUserFragment)
    }

    private fun launchUserLoggedTrueFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_userLoggedTrueMenu)
    }

    private fun launchAboutFragment() {
        findNavController().navigate(R.id.action_userLoggedFalseMenu_to_aboutFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}