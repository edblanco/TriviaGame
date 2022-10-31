package com.dosparta.triviagame.screens.common.popups

import android.app.Activity
import android.content.Context
import com.dosparta.triviagame.R
import com.google.android.material.snackbar.Snackbar

class OverlayMessagesHelper(private val context: Context) {
    fun showGameOverOverlay(correctAnswers: Int, questionsAnswered: Int) {
        Snackbar.make((context as Activity).window.decorView.rootView, context.getString(R.string.game_over_overlay, correctAnswers, questionsAnswered)
            , Snackbar.LENGTH_SHORT).show()
    }
}