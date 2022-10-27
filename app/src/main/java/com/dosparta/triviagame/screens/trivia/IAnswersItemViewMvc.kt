package com.dosparta.triviagame.screens.trivia

import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.ViewMvc

interface IAnswersItemViewMvc: ViewMvc{
    interface Listener {
        fun onAnswerClicked(answer: Answer, viewMvc: IAnswersItemViewMvc)
    }
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun updateTintColor(isCorrect: Boolean)
    fun bindAnswer(answer: Answer)
}