package com.dosparta.triviagame.screens.common.popups

interface AlertDialogListener {
    fun onPositiveAnswer(value: String?)
    fun onNegativeAnswer()
}