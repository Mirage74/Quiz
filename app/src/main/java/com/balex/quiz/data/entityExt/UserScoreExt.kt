package com.balex.quiz.data.entityExt

import com.balex.quiz.domain.entity.UserScore

class UserScoreExt{

    companion object {

        fun getEmptyInstance(): UserScore {
            return UserScore("", 0, "", "")
        }

        fun getEmptyInstanceWithUserName(userName: String): UserScore {
            return UserScore(userName, 0, "", "")
        }
    }
}