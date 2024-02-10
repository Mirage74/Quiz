package com.balex.quiz.domain.usecases

import androidx.lifecycle.LiveData
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.domain.repository.QuizRepository

class GetUserScoreUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(): LiveData<UserScore> {
        return quizRepository.getUserScore()
    }
}

