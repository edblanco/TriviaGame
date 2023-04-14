package com.dosparta.triviagame.screens.trivia.answersitem

import com.dosparta.triviagame.R

class UIAnswersItemConfig : IUIAnswersItemConfig {
    override fun getAnswerTintColor(isCorrect: Boolean) = if (isCorrect) R.color.hunter_green else R.color.blood_red
}
