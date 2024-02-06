package com.balex.quiz.domain.usecases

import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.repository.QuizRepository

class GenerateQuestionUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(level: Level, questionNumber: Int): Question {
        return quizRepository.generateQuestion(level, questionNumber)
    }
}