package com.balex.quiz.presentation

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.balex.quiz.data.NOT_LOGGED_USER
import com.balex.quiz.data.SHARED_PREFS
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_BEST_RES_POINTS
import com.balex.quiz.data.SHARED_PREFS_LAST_RES_CONTENT
import com.balex.quiz.data.SHARED_PREFS_USERNAME
import com.balex.quiz.domain.entity.UserScore

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context? = null
        private val notLogUserScoreInstance = UserScore(NOT_LOGGED_USER, 0, "", "")
        private fun getAppContext(): Context? {
            return context
        }

        fun saveDataUser(user: UserScore) {
            val sharedPreferences = context?.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            sharedPreferences?.let {
                val editor = sharedPreferences.edit()
                editor.putString(SHARED_PREFS_USERNAME, user.userName)
                editor.putInt(SHARED_PREFS_BEST_RES_POINTS, user.bestScore)
                editor.putString(SHARED_PREFS_BEST_RES_CONTENT, user.bestResultJSON)
                editor.putString(SHARED_PREFS_LAST_RES_CONTENT, user.lastResultJSON)
                editor.apply()
            }
        }

        private fun loadUserNameFromPrefs(): String {
            val sharedPreferences = context?.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            var userName = NOT_LOGGED_USER
            sharedPreferences?.let {
                userName = sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER).toString()
            }
            return userName
        }

        fun loadUserScore(): UserScore {
            val sharedPreferences = context?.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            val userName = loadUserNameFromPrefs()
            var userScore =  notLogUserScoreInstance
            sharedPreferences?.let {
                userScore = UserScore(
                    userName,
                    sharedPreferences.getInt(SHARED_PREFS_BEST_RES_POINTS, 0),
                    sharedPreferences.getString(SHARED_PREFS_BEST_RES_CONTENT, "").toString(),
                    sharedPreferences.getString(SHARED_PREFS_LAST_RES_CONTENT, "").toString()
                )
            }
            return userScore
        }
    }


    //    private val repository: BooksRepositoryImpl
//        get() = ServiceLocator.provideBooksRepository(this)
//
//
//    (applicationContext as App).repository.что-тотам()

}
