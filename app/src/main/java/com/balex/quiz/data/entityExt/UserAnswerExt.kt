package com.balex.quiz.data.entityExt

import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.UserAnswer

class UserAnswerExt(private val userAnswer: UserAnswer)  {


    fun getCountryName(countriesList: List<Country>): String {
        return countriesList.stream()
            .filter { e -> e.id == this.userAnswer.questionId }
            .findFirst().get().countryName.trim()
    }

    fun getCapitalNameRightAnswer(countriesList: List<Country>): String {
        return countriesList.stream()
            .filter { e -> e.id == this.userAnswer.questionId }
            .findFirst().get().capitalName.trim()
    }

    fun getCapitalNameUserAnswer(countriesList: List<Country>): String {
        return if (userAnswer.answerId > 0) {
            countriesList.stream()
                .filter { e -> e.id == this.userAnswer.answerId }
                .findFirst().get().capitalName.trim()
        } else {
            TIME_EXPIRED
        }
    }

    fun serializeInstance(): String {
        return "{${userAnswer.frameNum}/${userAnswer.questionId}/${userAnswer.answerId}/${userAnswer.score}}"
    }

    companion object {

        const val TIME_EXPIRED = "No answer"

        fun getQuizScore(list: List<UserAnswer>): Int {
            return list.map{it -> it.score}.sum()
        }

        fun getEmptyInstance(): UserAnswer {
            return UserAnswer(1, 1, 1, 1)
        }

        fun deserializeInstance(s: String): UserAnswer {
            var temp = s
            val frameNum = s.substring(temp.indexOf("{") + 1, temp.indexOf("/")).toInt()
            temp = temp.substring(temp.indexOf("/") + 1)
            val questionId = temp.substring(0, temp.indexOf("/")).toInt()
            temp = temp.substring(temp.indexOf("/") + 1)
            val answerId = temp.substring(0, temp.indexOf("/")).toInt()
            temp = temp.substring(temp.indexOf("/") + 1)
            val score = temp.substring(0, temp.indexOf("}")).toInt()
            return UserAnswer(frameNum, questionId, answerId, score)
        }

        fun serializeListOfInstances(list: List<UserAnswer>): String {
            var s = ""
            for (i in 0..list.size - 1) {
                s += UserAnswerExt(list[i]).serializeInstance()
            }
            return s
        }

        fun deserializeListOfInstances(listSerialized: String): List<UserAnswer> {
            val listAnswers = mutableListOf<UserAnswer>()
            if (listSerialized.isNotEmpty()) {
                var s = listSerialized
                while (s.length > 5) {
                    val i = s.indexOf("}")
                    val temp = s.substring(0, i + 1)
                    listAnswers.add(deserializeInstance(temp))
                    s = s.substring(i + 1)
                }
            } else {
                throw RuntimeException("fun deserializeListOfInstances exception, listSerialized can not be empty!")
            }
            return listAnswers
        }
    }
}
