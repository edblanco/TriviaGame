package com.dosparta.triviagame.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dosparta.triviagame.screens.trivia.AnswersItemViewMvc
import com.dosparta.triviagame.screens.trivia.IAnswersItemViewMvc
import com.dosparta.triviagame.screens.trivia.ITriviaGameViewMvc
import com.dosparta.triviagame.screens.trivia.TriviaGameViewMvc

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {
    fun getTriviaGameViewMvc(parent: ViewGroup?): ITriviaGameViewMvc {
        return TriviaGameViewMvc(layoutInflater, parent, this)
    }

    fun getAnswersItemViewMvc(parent: ViewGroup?): IAnswersItemViewMvc {
        return AnswersItemViewMvc(layoutInflater, parent)
    }
}