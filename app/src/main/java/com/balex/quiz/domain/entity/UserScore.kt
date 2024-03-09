package com.balex.quiz.domain.entity

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken


data class UserScore(
    @SerializedName("DISPLAYNAME")
    val userName: String,
    @SerializedName("BESTSCORE")
    val bestScore: Int,
    @SerializedName("BEST_RES")
    val bestResultJSON: String,
    @SerializedName("LAST_RES")
    val lastResultJSON: String
) {
    fun serializeInstance(): String {
        return Gson().toJson(this)
    }
    companion object {

        fun getEmptyInstance(): UserScore {
            return UserScore("", 0, "", "")
        }

        fun deserializeInstance(s: String): UserScore  {
            return Gson().fromJson(s, UserScore::class.java)
        }
    }
}
