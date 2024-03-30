package com.balex.quiz.di

import android.app.Application
import androidx.lifecycle.ViewModel
import com.balex.quiz.presentation.GameCoreViewModel
import com.balex.quiz.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(GameCoreViewModel::class)
    fun bindGameCoreViewModel(viewModel: GameCoreViewModel): ViewModel


}