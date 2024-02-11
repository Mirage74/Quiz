package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.databinding.ChooseLevelBinding
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class ChooseLevelFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var userScore: UserScore

    private var _binding: ChooseLevelBinding? = null
    private val binding: ChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChooseLevelBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userScore = viewModel.notLogUserScoreInstance
        viewModel.userScore.value?.let { userScore = it }
        val textGreeting =
            "Hello, ${userScore.userName.lowercase().replaceFirstChar { c -> c.uppercase() }}!"

        binding.tvHelloUser?.text = textGreeting
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
//        findNavController().navigate(
//            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
//        )
    }

}