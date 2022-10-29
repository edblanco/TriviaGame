package com.dosparta.triviagame.screens.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dosparta.triviagame.screens.trivia.TriviaGameViewMvc

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {
    fun getTriviaGameViewMvc(parent: ViewGroup?): TriviaGameViewMvc {
        return TriviaGameViewMvc(layoutInflater, parent)
    }
}