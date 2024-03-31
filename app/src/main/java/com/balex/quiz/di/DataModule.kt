package com.balex.quiz.di

import com.balex.quiz.data.QuizRepositoryImpl
import com.balex.quiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindQuizRepository(impl: QuizRepositoryImpl): QuizRepository

}