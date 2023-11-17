package com.dosparta.triviagame.screens.trivia.answersitem

interface IUIAnswersItemConfig {
    fun getAnswerTintColor(isCorrect: Boolean, isDarkModeActive: Boolean): Int
}