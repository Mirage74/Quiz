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
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class UserLoggedTrueMenuFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var userName = ""
    private var _binding: StatusUserLoggedTrueBinding? = null
    private val binding: StatusUserLoggedTrueBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusUserLoggedTrueBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        observeViewModel()
        setClickListeners()
    }

    private fun initViewValues() {
        userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        with(binding) {
            username.text = userName
            startTest.isEnabled = false

        }
    }

    private fun observeViewModel() {
        viewModel.isListCountriesFromBackendLoaded.observe(viewLifecycleOwner) {
            if (it) {
                binding.startTest.isEnabled = true
            }
        }
    }

    private fun setClickListeners() {
        with(binding) {
            startTest.setOnClickListener {
                launchChooseLevelFragment()
            }

            about.setOnClickListener{
                launchAboutFragment()
            }

            logout.setOnClickListener {
                App.setUserNotLogged(requireActivity().application)
                viewModel.setIsUserLogged(false)
                launchUserLoggedFalseFragment()
            }
        }
    }


    private fun launchChooseLevelFragment() {
        findNavController().navigate(R.id.action_userLoggedTrueMenu_to_chooseLevelFragment)
    }

    private fun launchUserLoggedFalseFragment() {
        findNavController().navigate(R.id.action_userLoggedTrueMenu_to_userLoggedFalseMenu)
    }

    private fun launchAboutFragment() {
        findNavController().navigate(R.id.action_userLoggedTrueMenu_to_aboutFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}