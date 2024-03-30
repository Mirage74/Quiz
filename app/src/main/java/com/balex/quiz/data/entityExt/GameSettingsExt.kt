package com.balex.quiz.data.entityExt

import com.balex.quiz.domain.entity.GameSettings

class GameSettingsExt() {
    companion object {
        fun getEmptyInstance(): GameSettings {
            return GameSettings(0, 0, 0, 0, 0, 0, 0)
        }
    }
}
