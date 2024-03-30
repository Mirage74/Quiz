package com.balex.quiz.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.balex.quiz.databinding.ChooseLevelBinding
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.QuizApp
import com.balex.quiz.presentation.ViewModelFactory
import javax.inject.Inject

class ChooseLevelFragment : Fragment() {

    private var userName = ""

    private lateinit var gameViewModel: GameCoreViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as QuizApp).component
    }


    private var _binding: ChooseLevelBinding? = null
    private val binding: ChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
        gameViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[GameCoreViewModel::class.java]
        initViewValues()
        setClickListeners()
    }

    private fun initViewValues() {
        userName = QuizApp.loadUserNameFromPrefsCapitalized(requireActivity().application)
        val textGreeting =
            "Hello, ${QuizApp.loadUserNameFromPrefsCapitalized(requireActivity().application)}!"

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