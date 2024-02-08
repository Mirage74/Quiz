package com.balex.quiz.domain.usecases

import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.domain.repository.QuizRepository

class RefreshUserScorePrefsUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(userScore: UserScore) {
        return quizRepository.refreshUserScore(userScore)
    }
}
