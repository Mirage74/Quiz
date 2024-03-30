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
import com.balex.quiz.databinding.CoreTestBinding
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.QuizApp
import com.balex.quiz.presentation.ViewModelFactory
import javax.inject.Inject


class GameCoreFragment : Fragment() {

    private lateinit var gameViewModel: GameCoreViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as QuizApp).component
    }


    private var _binding: CoreTestBinding? = null
    private val binding: CoreTestBinding
        get() = _binding ?: throw RuntimeException("GameCoreFragment == null")


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoreTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[GameCoreViewModel::class.java]
        var currQuestionNotNull = 0
        gameViewModel.currentQuestionNumber.value?.let {
            currQuestionNotNull = it
        }
        gameViewModel._currentQuestionString.value =
            "$currQuestionNotNull / ${gameViewModel.gameSettings.allQuestions}"

        if (currQuestionNotNull > 0 && currQuestionNotNull <= gameViewModel.gameSettings.allQuestions) {
            with(binding) {
                viewModel = gameViewModel
                lifecycleOwner = viewLifecycleOwner
            }
            initViewValues()
            observeViewModel()
            binding.ivImageCapital.setImageBitmap(gameViewModel.bitmapImagesList[currQuestionNotNull - 1])
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
            username.text = QuizApp.loadUserNameFromPrefsCapitalized(requireActivity().application)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
