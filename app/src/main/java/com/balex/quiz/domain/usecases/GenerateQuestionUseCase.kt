package com.balex.quiz.domain.usecases

import androidx.lifecycle.LiveData
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.repository.QuizRepository

class GenerateQuestionUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(level: Level): Question {
        return quizRepository.generateQuestion(level)
    }
}