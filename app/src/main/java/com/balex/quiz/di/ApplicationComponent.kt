package com.balex.quiz.di

import android.app.Application
import com.balex.quiz.presentation.MainActivity
import com.balex.quiz.presentation.MainViewModel
import com.balex.quiz.presentation.fragments.ChooseLevelFragment
import com.balex.quiz.presentation.fragments.ProgressLoadingFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: ChooseLevelFragment)

    fun inject(activity: ProgressLoadingFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}