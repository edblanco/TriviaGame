package com.dosparta.triviagame.screens.common.dialogs.promptdialog

// todo give id to know that you're dealing with prompt you want
class PromptDialogEvent(private val clickedButton: Button) {

    enum class Button {
        POSITIVE, NEGATIVE
    }

    fun getClickedButton(): Button{
        return clickedButton
    }

}