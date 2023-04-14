package com.dosparta.triviagame.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dosparta.triviagame.screens.trivia.answersitem.AnswersItemViewMvc
import com.dosparta.triviagame.screens.trivia.answersitem.IAnswersItemViewMvc
import com.dosparta.triviagame.screens.trivia.ITriviaGameViewMvc
import com.dosparta.triviagame.screens.trivia.TriviaGameViewMvc
import com.dosparta.triviagame.screens.trivia.answersitem.IUIAnswersItemConfig
import com.dosparta.triviagame.screens.trivia.answersitem.UIAnswersItemConfig

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {
    fun getTriviaGameViewMvc(parent: ViewGroup?): ITriviaGameViewMvc {
        return TriviaGameViewMvc(layoutInflater, parent, this)
    }

    fun getAnswersItemViewMvc(parent: ViewGroup?): IAnswersItemViewMvc {
        return AnswersItemViewMvc(layoutInflater, parent, getUIAnswersItemConfig())
    }

    private fun getUIAnswersItemConfig(): IUIAnswersItemConfig {
        return UIAnswersItemConfig()
    }
}