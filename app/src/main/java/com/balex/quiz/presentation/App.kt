package com.balex.quiz.presentation

import android.app.Application
import com.balex.quiz.data.SHARED_PREFS
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_POINTS
import com.balex.quiz.data.SHARED_PREFS_LAST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_USERNAME
import com.balex.quiz.domain.entity.UserScore

class App : Application() {
    companion object {
        fun saveDataUser(user: UserScore, application: Application) {
            val sharedPreferences = application.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString(SHARED_PREFS_USERNAME, user.userName)
            editor.putInt(SHARED_PREFS_BEST_RES_POINTS, user.bestScore)
            editor.putString(SHARED_PREFS_BEST_RES_CONTENT, user.bestResultJSON)
            editor.putString(SHARED_PREFS_LAST_RES_CONTENT, user.lastResultJSON)
            editor.apply()
        }
    }


    //    private val repository: BooksRepositoryImpl
//        get() = ServiceLocator.provideBooksRepository(this)
//
//
//    (applicationContext as App).repository.что-тотам()

}
