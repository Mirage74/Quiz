package com.balex.quiz.presentation

import android.app.Application
import android.content.Context
import com.balex.quiz.domain.entity.UserScore

const val SHARED_PREFS = "shared_prefs"
const val SHARED_PREFS_USERNAME = "shared_prefs_username"
const val SHARED_PREFS_BEST_RES_POINTS = "shared_prefs_best_res_points"
const val SHARED_PREFS_BEST_RES_CONTENT = "shared_prefs_best_res_content"
const val SHARED_PREFS_LAST_RES_CONTENT = "shared_prefs_last_res_content"
const val NOT_LOGGED_USER = "notLoggedUser"

class App : Application() {

    companion object {


        fun saveDataUser(user: UserScore, context: Context) {
            val sharedPreferences = context.getSharedPreferences(
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

        fun setUserNotLogged(context: Context) {
            val sharedPreferences = context.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.putString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER)
            editor.putInt(SHARED_PREFS_BEST_RES_POINTS, 0)
            editor.putString(SHARED_PREFS_BEST_RES_CONTENT, "")
            editor.putString(SHARED_PREFS_LAST_RES_CONTENT, "")
            editor.apply()

        }

//        fun loadUserNameFromPrefs(context: Context): String {
//            val sharedPreferences = context.getSharedPreferences(
//                SHARED_PREFS,
//                MODE_PRIVATE
//            )
//            return sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER).toString()
//        }

        fun loadUserNameFromPrefsCapitalized(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            return sharedPreferences.getString(SHARED_PREFS_USERNAME, NOT_LOGGED_USER)?.lowercase()
                ?.replaceFirstChar(Char::uppercase).toString().trim()
        }

        fun loadUserScore(context: Context): UserScore {
            val sharedPreferences = context.getSharedPreferences(
                SHARED_PREFS,
                MODE_PRIVATE
            )
            val userName = loadUserNameFromPrefsCapitalized(context)

            return UserScore(
                userName,
                sharedPreferences.getInt(SHARED_PREFS_BEST_RES_POINTS, 0),
                sharedPreferences.getString(SHARED_PREFS_BEST_RES_CONTENT, "").toString(),
                sharedPreferences.getString(SHARED_PREFS_LAST_RES_CONTENT, "").toString()
            )
        }
    }

}
