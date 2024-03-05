package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.balex.quiz.databinding.ProgressBarBinding
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class ProgressLoadingFragment : Fragment() {
    private val args by navArgs<GameCoreFragmentArgs>()
    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application, args.levelEnum)
    }

    private val gameViewModel by lazy {
        ViewModelProvider(requireActivity(), gameViewModelFactory)[GameCoreViewModel::class.java]
    }


    private var _binding: ProgressBarBinding? = null
    private val binding: ProgressBarBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

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
        val mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
        with(binding) {
            viewModel = gameViewModel
            lifecycleOwner = viewLifecycleOwner
        }
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
                gameViewModel.resetNewTestData()
                launchGameFragment(args.levelEnum)
            }

        }
    }

    private fun launchGameFragment(level: Level) {
        findNavController().navigate(
            ProgressLoadingFragmentDirections.actionProgressLoadingFragmentToGameCoreFragment(level)
        )
    }
}