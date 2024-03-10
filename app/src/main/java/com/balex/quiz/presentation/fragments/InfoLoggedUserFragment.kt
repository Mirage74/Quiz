package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.balex.quiz.R
import com.balex.quiz.databinding.InfoBinding
import com.balex.quiz.presentation.App

class InfoLoggedUserFragment : Fragment() {
    private var _binding: InfoBinding? = null
    private val binding: InfoBinding
        get() = _binding ?: throw RuntimeException("InfoLoggedUserFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        setClickListeners()
    }

    private fun initViewValues() {
        val userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        with(binding) {
            tvUsername.text = userName

        }
    }

    private fun setClickListeners() {
        with(binding) {
            btViewResult.setOnClickListener {
                launchViewResultFragment()
            }
            btTestRules.setOnClickListener {
                launchQuizRulesFragment()
            }

        }
    }

    private fun launchViewResultFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_resultQuizFragment)
    }

    private fun launchQuizRulesFragment() {
        findNavController().navigate(R.id.action_infoLoggedUserFragment_to_gameRulesFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}