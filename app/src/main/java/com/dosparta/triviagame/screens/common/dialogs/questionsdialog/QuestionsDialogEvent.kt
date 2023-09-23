package com.dosparta.triviagame.screens.common.dialogs.questionsdialog

import com.dosparta.triviagame.categories.TriviaCategoryIn

class QuestionsDialogEvent(private val questionsAmount: String, val category: TriviaCategoryIn) {

    fun getQuestionsAmount(): String {
        return questionsAmount
    }

}