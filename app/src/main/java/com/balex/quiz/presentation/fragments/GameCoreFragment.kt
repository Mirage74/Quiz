package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.balex.quiz.R
import com.balex.quiz.databinding.CoreTestBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel


class GameCoreFragment : Fragment() {
    //private val TAG = "GameCoreFragment"
    private val args by navArgs<GameCoreFragmentArgs>()
    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application, args.levelEnum)
    }

    private val gameViewModel by lazy {
        ViewModelProvider(requireActivity(), gameViewModelFactory)[GameCoreViewModel::class.java]
    }


    private var _binding: CoreTestBinding? = null
    private val binding: CoreTestBinding
        get() = _binding ?: throw RuntimeException("GameCoreFragment == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoreTestBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        var currQuestionNotNull = 0
        gameViewModel.currentQuestionNumber.value?.let {
            currQuestionNotNull = it
        }
        gameViewModel._currentQuestionString.value =
            "1 / ${gameViewModel.gameSettings.allQuestions}"
        if (currQuestionNotNull > 0 && currQuestionNotNull <= gameViewModel.gameSettings.allQuestions) {
            with(binding) {
                viewModel = gameViewModel
                lifecycleOwner = viewLifecycleOwner
            }
            initViewValues()
            observeViewModel()
            if (!gameViewModel.isRoundInProgress) {
                gameViewModel.startTimer()
                gameViewModel.isRoundInProgress = true
            }


        } else {
            throw RuntimeException("Question number ${gameViewModel.currentQuestionNumber} is not in range 1..${gameViewModel.gameSettings.allQuestions} !")
        }
    }


    private fun initViewValues() {
        gameViewModel.isQuizFinished.value = false
        gameViewModel.lockButtons = false
        with(binding) {
            username.text = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        }

    }

    private fun observeViewModel() {
        gameViewModel.isQuizFinished.observe(viewLifecycleOwner) {
            if (it) {
                launchQuizResFragment()
            }

        }

    }

    private fun launchQuizResFragment() {
        findNavController().navigate(R.id.action_gameCoreFragment_to_resultQuizFragment)
    }

}
