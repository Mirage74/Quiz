package com.balex.quiz.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GameSettings(
    val allQuestions: Int,
    val timeMaxSec: Int,
    val timeRestBestScoreSec: Int,
    val timeRestMediumScoreSec: Int,
    val pointsMax: Int,
    val pointsMedium: Int,
    val pointsMin: Int
) : Parcelable {
    companion object {
        fun getEmptyInstance(): GameSettings {
            return GameSettings(0, 0, 0, 0, 0, 0, 0)
        }
    }
}