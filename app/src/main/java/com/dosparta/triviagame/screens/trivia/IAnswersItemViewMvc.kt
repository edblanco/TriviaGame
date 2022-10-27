package com.dosparta.triviagame.screens.trivia

import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.ObservableViewMvc

interface IAnswersItemViewMvc: ObservableViewMvc<IAnswersItemViewMvc.Listener>{
    interface Listener {
        fun onAnswerClicked(answer: Answer, viewMvc: IAnswersItemViewMvc)
    }
    fun updateTintColor(isCorrect: Boolean)
    fun bindAnswer(answer: Answer)
}