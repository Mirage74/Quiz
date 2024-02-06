package com.balex.quiz.domain.usecases

import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.repository.QuizRepository

class GetGameSettingsUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(level: Level): GameSettings {
        return quizRepository.getGameSettings(level)
    }
}