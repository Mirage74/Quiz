package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.databinding.InfoBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class InfoLoggedUserFragment : Fragment() {
    private var _binding: InfoBinding? = null
    private val binding: InfoBinding
        get() = _binding ?: throw RuntimeException("InfoLoggedUserFragment == null")

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoBinding.inflate(inflater, container, false)
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


    private fun observeViewModel() {
        if (viewModel.isListCountriesFromBackendLoaded.value == false) {

            binding.btGuide.isEnabled = false
            viewModel.isListCountriesFromBackendLoaded.observe(viewLifecycleOwner) {
                if (it) {
                    binding.btGuide.isEnabled = true
                }
            }
        }
    }


    private fun initViewValues() {
        val userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        with(binding) {
            tvUsername.text = userName

        }
    }

    private fun setClickListeners() {
        with(binding) {
            startTest.setOnClickListener {
                launchChooseLevelFragment()
            }
            btViewResult.setOnClickListener {
                launchViewResultFragment()
            }
            btTestRules.setOnClickListener {
                launchQuizRulesFragment()
            }
            btGuide.setOnClickListener {
                launchCountriesListGuideFragment()
            }

        }
    }

    private fun launchChooseLevelFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_chooseLevelFragment)
    }

    private fun launchViewResultFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_resultQuizFragment)
    }

    private fun launchQuizRulesFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_gameRulesFragment)
    }

    private fun launchCountriesListGuideFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_recycledListCountriesFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}