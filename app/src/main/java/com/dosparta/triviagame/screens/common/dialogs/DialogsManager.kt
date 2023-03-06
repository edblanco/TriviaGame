package com.dosparta.triviagame.screens.common.dialogs

import android.content.Context
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.promptdialog.PromptDialog
import com.dosparta.triviagame.screens.common.dialogs.questionsdialog.QuestionsDialog
import com.dosparta.triviagame.screens.common.popups.AlertDialogListener

class DialogsManager(private val context: Context, private val fragmentManager: FragmentManager) {

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
                answerListener.onPositiveAnswer(null)
            }
            .setNegativeButton(R.string.game_over_popup_negative_msg){ dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }

    fun showErrorDialog(statusCode: Int, message: Int, answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.network_error_popup_title, statusCode))
            .setMessage(message)
            .setPositiveButton(R.string.network_error_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onPositiveAnswer(null)
            }
            .setNegativeButton(R.string.network_error_popup_negative_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }

    // todo make it custom with a drop box selection
    fun getAmountOfQuestionsDialog(answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setTitle(context.getString(R.string.amount_of_questions_title))
            .setView(input)
            .setMessage(context.getString(R.string.amount_of_question_message))
            .setPositiveButton(R.string.game_over_popup_positive_msg) { dialog, _ ->
                answerListener.onPositiveAnswer(input.text.toString())
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    fun showQuestionsAmountUseCaseDialog(tag: String?){
        val dialogFragment = QuestionsDialog.newQuestionsDialog()
        dialogFragment.show(fragmentManager, tag)
    }

    fun showErrorUseCaseDialog(statusCode: Int, messageResourceID: Int, tag: String?) {
        val title = context.getString(R.string.network_error_popup_title, statusCode)
        val messageText = context.getString(messageResourceID)
        val captionOKText = context.getString(R.string.network_error_popup_positive_msg)
        val captionNOKText = context.getString(R.string.network_error_popup_negative_msg)
        val dialogFragment = PromptDialog.newPromptDialog(title, messageText, captionOKText, captionNOKText)
        dialogFragment.show(fragmentManager, tag)
    }

    fun showResultsUseCaseDialog(correctAnswers: Int, totalAnswers: Int, tag: String?) {
        val correctAnswersOverTotal = (correctAnswers * HUNDRED_PERCENT) / totalAnswers
        val dialogFragment = PromptDialog.newPromptDialog(context.getString(R.string.game_over_popup_title), context.getString(R.string.game_over_popup_msg, correctAnswers, totalAnswers, correctAnswersOverTotal), context.getString(R.string.game_over_popup_positive_msg), context.getString(R.string.game_over_popup_negative_msg))
        dialogFragment.show(fragmentManager, tag)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments){
            if (fragment is BaseDialog){
                return fragment.tag
            }
        }
        return null
    }
}