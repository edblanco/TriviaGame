package com.dosparta.triviagame.screens.common.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.popups.AlertDialogListener

class DialogManager(private val context: Context) {

    companion object {
        private const val HUNDRED_PERCENT = 100
    }

    fun showResults(correctAnswers: Int, totalAnswers: Int, answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(context)
        val correctAnswersOverTotal = (correctAnswers * HUNDRED_PERCENT) / totalAnswers
        builder.setTitle(R.string.game_over_popup_title)
            .setMessage(context.getString(R.string.game_over_popup_msg, correctAnswers, totalAnswers, correctAnswersOverTotal))
            .setPositiveButton(R.string.game_over_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onPositiveAnswer()
            }
            .setNegativeButton(R.string.game_over_popup_negative_msg){ dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }

    fun showErrorDialog(statusCode: Int, answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.network_error_popup_title, statusCode))
            .setMessage(R.string.network_error_popup_msg)
            .setPositiveButton(R.string.network_error_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onPositiveAnswer()
            }
            .setNegativeButton(R.string.network_error_popup_negative_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }
}