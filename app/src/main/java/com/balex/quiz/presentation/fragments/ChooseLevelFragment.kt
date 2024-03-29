package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.databinding.ChooseLevelBinding
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel

class ChooseLevelFragment : Fragment() {

    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application)
    }

    private val gameViewModel by lazy {
        ViewModelProvider(requireActivity(), gameViewModelFactory)[GameCoreViewModel::class.java]
    }

    private var userName = ""

    private var _binding: ChooseLevelBinding? = null
    private val binding: ChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChooseLevelBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        setClickListeners()
    }
    private fun initViewValues() {
        userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        val textGreeting =
            "Hello, ${App.loadUserNameFromPrefsCapitalized(requireActivity().application)}!"

        binding.tvHelloUser?.text = textGreeting
    }
    private fun setClickListeners() {
        with(binding) {
            easy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            medium.setOnClickListener {
                launchGameFragment(Level.MEDIUM)
            }
            hard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        gameViewModel.level = level
        gameViewModel.setGameSettings(level)
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToProgressLoadingFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}