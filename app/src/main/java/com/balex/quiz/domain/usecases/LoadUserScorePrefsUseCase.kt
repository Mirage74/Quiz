package com.balex.quiz.domain.usecases

import com.balex.quiz.domain.repository.QuizRepository

class LoadUserScorePrefsUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke() {
        return quizRepository.loadUserScore()
    }
}

