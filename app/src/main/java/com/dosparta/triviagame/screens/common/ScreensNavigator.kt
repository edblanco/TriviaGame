package com.dosparta.triviagame.screens.common

import android.app.Activity
import android.content.Context
import com.dosparta.triviagame.screens.trivia.TriviaGameActivity

class ScreensNavigator(private val context: Context) {
    fun toTriviaGame() {
        TriviaGameActivity.start(context)
    }

    fun closeScreen() {
        (context as Activity).finish()
    }

    fun closeApp() {
        (context as Activity).finishAffinity()
    }
}
