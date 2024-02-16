package com.balex.quiz.presentation.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.databinding.CoreTestBinding
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameCoreFragment : Fragment() {
    private val TAG = "GameCoreFragment"
    private val args by navArgs<GameCoreFragmentArgs>()
    private val gameViewModelFactory by lazy {

        GameCoreModelFactory(requireActivity().application, args.levelEnum)
    }

    private val gameViewModel by lazy {
        ViewModelProvider(this, gameViewModelFactory)[GameCoreViewModel::class.java]
    }

    var bitmapNextQuestion: Bitmap? = null

    private var userName = ""
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
        gameViewModel.countriesNotUsedInQuestion = mainViewModel.countriesFullList
        gameViewModel.pr()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        observeViewModel()
    }


    private fun initViewValues() {
        userName = App.loadUserNameFromPrefsCapitalized(requireActivity().application)
        //binding.ivImageCapital.setImageResource(R.drawable.eye)
    }

private fun observeViewModel() {
    gameViewModel.imageName.observe(viewLifecycleOwner) {
        Glide.with(this)
            .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${it}")
            .into(binding.ivImageCapital)




        val str = "Uzbekistan-Tashkent.jpg"

        CoroutineScope(Dispatchers.IO).launch {
            bitmapNextQuestion = Glide.with(requireActivity())
                .asBitmap()
                .load("${ApiFactory.BASE_URL_STATIC_IMAGES}/${str}")
                .submit().get()

        }

        val handler = Handler()
        handler.postDelayed({
            Glide.with(requireActivity())
                .load(bitmapNextQuestion)
                .into(binding.ivImageCapital)
        }, 5000)



        //Picasso.get().load("${ApiFactory.BASE_URL}/${ApiFactory.BACKEND_STATIC_IMAGES_PREFIX}/".into(ivImageCapital)

    }
}

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
