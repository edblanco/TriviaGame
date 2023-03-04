package com.dosparta.triviagame.screens.common.dialogs.questionsdialog

class QuestionsDialogEvent(private val questionsAmount: String) {

    fun getQuestionsAmount(): String {
        return questionsAmount
    }
}