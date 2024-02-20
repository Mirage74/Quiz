package com.balex.quiz.presentation.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.balex.quiz.databinding.CoreTestBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.delay

class GameCoreFragment : Fragment() {
    private val TAG = "GameCoreFragment"
    private val args by navArgs<GameCoreFragmentArgs>()
    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application, args.levelEnum)
    }

    private val gameViewModel by lazy {
        ViewModelProvider(this, gameViewModelFactory)[GameCoreViewModel::class.java]
    }


    private var _binding: CoreTestBinding? = null
    private val binding: CoreTestBinding
        get() = _binding ?: throw RuntimeException("UserLoggedFalseMenu == null")

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CoreTestBinding.inflate(inflater, container, false)

        val mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]

        gameViewModel.countriesFullList = mainViewModel.countriesFullList
        gameViewModel.setQuestionList()
        gameViewModel.downloadImagesToBitmap()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        observeViewModel()
    }


    private fun initViewValues() {
        binding.username.text = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
    }

    private fun observeViewModel() {
        gameViewModel.isImagesDownloaded.observe(viewLifecycleOwner) {
            if (it) {
                binding.ivImageCapital.setImageBitmap(gameViewModel.bitmapImagesList[10])
                Log.d("ddd", "observeViewModel size ${gameViewModel.bitmapImagesList.size}")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
