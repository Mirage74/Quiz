package com.balex.quiz.presentation.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.balex.quiz.R
import com.balex.quiz.databinding.QuizResultBinding
import com.balex.quiz.domain.entity.UserAnswer
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.presentation.App
import com.balex.quiz.presentation.GameCoreModelFactory
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.MainViewModelFactory
import java.util.Collections
import java.util.function.Predicate

class ResultQuizFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var _binding: QuizResultBinding? = null
    private val binding: QuizResultBinding
        get() = _binding ?: throw RuntimeException("ResultQuizFragment == null")

    private var userInfo = UserScore("", 0, "", "")
    private var listLastScore : MutableList<UserAnswer> = Collections.emptyList()
    private var listBestScore : MutableList<UserAnswer> = Collections.emptyList()
    private var showMode = LAST_RES_SHOW_MODE


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuizResultBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(requireActivity().application))[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
        userInfo = App.loadUserScore(requireActivity())
        listLastScore =
            UserAnswer.deserializeListOfInstances(userInfo.lastResultJSON).sortedBy { it.frameNum }.toMutableList()
        listBestScore =
            UserAnswer.deserializeListOfInstances(userInfo.bestResultJSON).sortedBy { it.frameNum }.toMutableList()
        binding.layoutTableResult.addView(fillAnswerTable())



//        Log.d("ff", listLastScore.toString())
//        Log.d("ff", listBestScore.toString())

    }

    private fun createOneRaw(userAnswer: UserAnswer, trParams: TableLayout.LayoutParams): TableRow {
        val tvQuizNum = TextView(requireActivity())
        val tvCountryName = TextView(requireActivity())
        val tvScorePoints = TextView(requireActivity())
        var s = ""
        tvQuizNum.setLayoutParams(
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        )
        tvQuizNum.setGravity(Gravity.END)
        tvQuizNum.setPadding(1, PADDING_VERTICAL, 0, PADDING_VERTICAL)
        s = userAnswer.frameNum.toString() + "   "
        tvQuizNum.text = s
        tvQuizNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_TEXT_SIZE.toFloat())

        tvCountryName.setLayoutParams(
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        )
        tvCountryName.setGravity(Gravity.START)
        tvCountryName.setPadding(5, PADDING_VERTICAL, 0, PADDING_VERTICAL)
        s = userAnswer.getCountryName(viewModel.countriesFullList)
        tvCountryName.setText(s)
        tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_TEXT_SIZE.toFloat())

        tvScorePoints.setLayoutParams(
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        )
        tvScorePoints.setGravity(Gravity.CENTER)
        tvScorePoints.setPadding(5, 15, 1, 15)
        s = userAnswer.score.toString()
        tvScorePoints.text = s
        if (userAnswer.score > 0) {
            tvScorePoints.setTextColor(Color.GREEN)
        } else {
            tvScorePoints.setTextColor(ContextCompat.getColor(requireActivity(), R.color.wrongAnswer))
        }
        tvScorePoints.setTextSize(TypedValue.COMPLEX_UNIT_SP,  SMALL_TEXT_SIZE.toFloat())

        val tableRaw = TableRow(requireActivity())
        tableRaw.setId(userAnswer.answerId)
        tableRaw.setPadding(0, 0, 0, 1)
        tableRaw.setLayoutParams(trParams)
        tableRaw.addView(tvQuizNum)
        tableRaw.addView(tvCountryName)
        tableRaw.addView(tvScorePoints)
        tableRaw.background = ResourcesCompat.getDrawable(resources, R.drawable.border, null);

        return tableRaw
    }

    private fun fillAnswerTable(): View {
        val scoreList = if (showMode == LAST_RES_SHOW_MODE) {
            listLastScore
        } else {
            listBestScore
        }

        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0

        val tableLayout = TableLayout(requireActivity())
        tableLayout.isStretchAllColumns = true

        val trParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        trParams.setMargins(
            leftRowMargin, topRowMargin, rightRowMargin,
            bottomRowMargin
        )

        for (i in 0..<scoreList.size) {
            tableLayout.addView(createOneRaw(scoreList[i], trParams))
        }
    return tableLayout
    }

    private fun initViewValues() {
        with(binding) {
            username.text = App.loadUserNameFromPrefsCapitalized(requireActivity().application)

        }

    }

    companion object {
        const val LAST_RES_SHOW_MODE = 0
        const val BEST_RES_SHOW_MODE = 1

        const val PADDING_VERTICAL = 15
        const val SMALL_TEXT_SIZE = 15
    }
}