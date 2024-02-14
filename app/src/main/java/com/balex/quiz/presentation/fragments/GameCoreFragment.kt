package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.balex.quiz.databinding.CoreTestBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.GameCoreModelFactory

class GameCoreFragment : Fragment() {
    private val TAG = "GameCoreFragment"
    private val args by navArgs<GameCoreFragmentArgs>()
    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application, args.levelEnum)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, gameViewModelFactory)[GameCoreViewModel::class.java]
    }

    private var userName = ""
    private var _binding: CoreTestBinding? = null
    private val binding: CoreTestBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoreTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, viewModel.getArg())
        initViewValues()
//        observeViewModel()
    }


    private fun initViewValues() {
        userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
    }

}
