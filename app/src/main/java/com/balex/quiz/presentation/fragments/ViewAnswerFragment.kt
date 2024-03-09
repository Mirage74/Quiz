package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.databinding.ViewAnswerFragmentBinding
import com.balex.quiz.domain.entity.UserAnswer
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory

class ViewAnswerFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: ViewAnswerFragmentBinding? = null
    private val binding: ViewAnswerFragmentBinding
        get() = _binding ?: throw RuntimeException("ViewAnswerFragmentBinding == null")

    private var countryName = ""
    private var capitalName = ""
    private var userAnswerCapitalName = ""
    private var score = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewAnswerFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userAnswer = UserAnswer.getEmptyInstance()
        viewModel.currentResultItemInView.value?.let {  userAnswer = it  }
        countryName = userAnswer.getCountryName(viewModel.countriesFullList)
        capitalName = userAnswer.getCapitalNameRightAnswer(viewModel.countriesFullList)
        userAnswerCapitalName = userAnswer.getCapitalNameUserAnswer(viewModel.countriesFullList)
        score = userAnswer.score.toString()
        binding.apply {
            if (userAnswer.score == 0) {
                tvYourAnswer.setTextColor(ContextCompat.getColor(requireActivity(), android.R.color.holo_red_dark))
            }
            var s = "Country name: $countryName"
            tvCountryName.text = s
            s = "Capital name: $capitalName"
            tvRightAnswer.text = s
            s = "Yours answer: $userAnswerCapitalName"
            tvYourAnswer.text = s
            s = "Points earned: $score"
            tvPoints.text = s
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}