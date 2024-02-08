package com.balex.quiz.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.balex.quiz.domain.entity.UserScore
import com.balex.quiz.domain.repository.QuizRepository

class GetUserScoreUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(): MutableLiveData<UserScore> {
        return quizRepository.getUserScore()
    }
}

