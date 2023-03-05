package com.dosparta.triviagame.screens.common.dialogs

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.promptdialog.PromptDialog
import com.dosparta.triviagame.screens.common.dialogs.questionsdialog.QuestionsDialog

class DialogsManager(private val context: Context, private val fragmentManager: FragmentManager) {

    companion object {
        private const val HUNDRED_PERCENT = 100
    }

    fun showQuestionsAmountUseCaseDialog(tag: String?, isCancellable: Boolean = true) {
        val dialogFragment = QuestionsDialog.newQuestionsDialog()
        dialogFragment.isCancelable = isCancellable
        dialogFragment.show(fragmentManager, tag)
    }

    fun showErrorUseCaseDialog(
        messageResourceID: Int,
        tag: String?,
        isCancellable: Boolean = true
    ) {
        val title = context.getString(R.string.network_error_popup_title)
        val messageText = context.getString(messageResourceID)
        val captionOKText = context.getString(R.string.network_error_popup_positive_msg)
        val captionNOKText = context.getString(R.string.network_error_popup_negative_msg)
        val dialogFragment =
            PromptDialog.newPromptDialog(title, messageText, captionOKText, captionNOKText)
        dialogFragment.isCancelable = isCancellable
        dialogFragment.show(fragmentManager, tag)
    }

    fun showResultsUseCaseDialog(
        correctAnswers: Int,
        totalAnswers: Int,
        tag: String?,
        isCancellable: Boolean = true
    ) {
        val correctAnswersOverTotal = (correctAnswers * HUNDRED_PERCENT) / totalAnswers
        val dialogFragment = PromptDialog.newPromptDialog(
            context.getString(R.string.game_over_popup_title),
            context.getString(
                R.string.game_over_popup_msg,
                correctAnswers,
                totalAnswers,
                correctAnswersOverTotal
            ),
            context.getString(R.string.game_over_popup_positive_msg),
            context.getString(R.string.game_over_popup_negative_msg)
        )
        dialogFragment.isCancelable = isCancellable
        dialogFragment.show(fragmentManager, tag)
    }

    fun getShownDialogTag(): String? {
        for (fragment in fragmentManager.fragments) {
            if (fragment is BaseDialog) {
                return fragment.tag
            }
        }
        return null
    }
}