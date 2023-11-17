package com.dosparta.triviagame.screens.trivia.answersitem

import com.dosparta.triviagame.R

class UIAnswersItemConfig : IUIAnswersItemConfig {
    override fun getAnswerTintColor(isCorrect: Boolean, isDarkModeActive: Boolean): Int {
        return if (isDarkModeActive){
            if (isCorrect) R.color.md_theme_dark_tertiary else R.color.md_theme_dark_error
        } else  {
            if (isCorrect) R.color.md_theme_light_tertiary else R.color.md_theme_light_error
        }
    }
}
