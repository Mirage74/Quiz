package com.balex.quiz.di

import android.app.Application
import com.balex.quiz.presentation.MainActivity
import com.balex.quiz.presentation.fragments.ChooseLevelFragment
import com.balex.quiz.presentation.fragments.GameCoreFragment
import com.balex.quiz.presentation.fragments.InfoLoggedUserFragment
import com.balex.quiz.presentation.fragments.LoginUserFragment
import com.balex.quiz.presentation.fragments.ProgressLoadingFragment
import com.balex.quiz.presentation.fragments.RegisterUserFragment
import com.balex.quiz.presentation.fragments.ResultQuizFragment
import com.balex.quiz.presentation.fragments.UserLoggedFalseMenuFragment
import com.balex.quiz.presentation.fragments.UserLoggedTrueMenuFragment
import com.balex.quiz.presentation.fragments.ViewAnswerFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: RegisterUserFragment)

    fun inject(activity: LoginUserFragment)

    fun inject(activity: UserLoggedFalseMenuFragment)

    fun inject(activity: UserLoggedTrueMenuFragment)

    fun inject(activity: InfoLoggedUserFragment)

    fun inject(activity: ChooseLevelFragment)

    fun inject(activity: ProgressLoadingFragment)

    fun inject(activity: GameCoreFragment)

    fun inject(activity: ResultQuizFragment)

    fun inject(activity: ViewAnswerFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}