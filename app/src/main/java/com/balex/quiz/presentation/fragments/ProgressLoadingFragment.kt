package com.balex.quiz.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.databinding.ProgressBarBinding
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import com.balex.quiz.presentation.QuizApp
import com.balex.quiz.presentation.ViewModelFactory
import javax.inject.Inject

class ProgressLoadingFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var gameViewModel: GameCoreViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    private var _binding: ProgressBarBinding? = null
    private val binding: ProgressBarBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    private val component by lazy {
        (requireActivity().application as QuizApp).component
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProgressBarBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[GameCoreViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        with(binding) {
            viewModel = gameViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        gameViewModel.resetNewTestData()
        observeViewModel()

        with (gameViewModel){
            currentQuestionNumber.value = 1
            getGameSettingsUseCase
            countriesFullList = mainViewModel.countriesFullList
            setQuestionList()
            downloadImagesToBitmap()
        }


    }

    private fun observeViewModel() {
        gameViewModel.isImagesDownloaded.observe(viewLifecycleOwner) {
            if (it) {
                //gameViewModel.resetNewTestData()
                launchGameFragment()
            }

        }
    }

    private fun launchGameFragment() {
        gameViewModel.userAnswers.clear()
        findNavController().navigate(
            ProgressLoadingFragmentDirections.actionProgressLoadingFragmentToGameCoreFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}