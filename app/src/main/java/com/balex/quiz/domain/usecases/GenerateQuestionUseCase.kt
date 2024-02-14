package com.balex.quiz.domain.usecases

import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.repository.QuizRepository

class GenerateQuestionUseCase(private val quizRepository: QuizRepository) {
    operator fun invoke(level: Level, questionNumber: Int, countriesListFull: List<Country>,
                        countriesListNotUsedInQuiz: List<Country>): Question {
        return quizRepository.generateQuestion(level, questionNumber, countriesListFull,
        countriesListNotUsedInQuiz)
    }
}