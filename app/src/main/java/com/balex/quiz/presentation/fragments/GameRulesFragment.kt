package com.balex.quiz.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.databinding.GameRulesBinding
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.usecases.GetGameSettingsUseCase

class GameRulesFragment : Fragment() {


    private var _binding: GameRulesBinding? = null
    private val binding: GameRulesBinding
        get() = _binding ?: throw RuntimeException("GameRulesFragment == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewValues()
    }

    private fun initViewValues() {
        val getGameSettingsUseCase = GetGameSettingsUseCase(QuizRepositoryImpl(requireActivity().application))
        val gameSettingEasy = getGameSettingsUseCase(Level.EASY)
        val gameSettingMedium = getGameSettingsUseCase(Level.MEDIUM)
        val gameSettingHard = getGameSettingsUseCase(Level.HARD)

        with(binding) {
            var s = "${gameSettingEasy.timeMaxSec - gameSettingEasy.timeRestBestScoreSec}"
            easySec.text = s
            s = "${gameSettingEasy.timeMaxSec - gameSettingEasy.timeRestMediumScoreSec}"
            mediumSec.text = s
            s = "${gameSettingEasy.timeMaxSec}"
            hardSec.text = s

            s = "${gameSettingEasy.pointsMax}"
            easyFastScore.text = s
            s = "${gameSettingEasy.pointsMedium}"
            easyMediumScore.text = s
            s = "${gameSettingEasy.pointsMin}"
            easySlowScore.text = s

            s = "${gameSettingMedium.pointsMax}"
            mediumFastScore.text = s
            s = "${gameSettingMedium.pointsMedium}"
            mediumMediumScore.text = s
            s = "${gameSettingMedium.pointsMin}"
            mediumSlowScore.text = s

            s = "${gameSettingHard.pointsMax}"
            hardFastScore.text = s
            s = "${gameSettingHard.pointsMedium}"
            hardMediumScore.text = s
            s = "${gameSettingHard.pointsMin}"
            hardSlowScore.text = s
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}