package com.dosparta.triviagame.screens.trivia.answersitem

import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.views.ObservableViewMvc

interface IAnswersItemViewMvc: ObservableViewMvc<IAnswersItemViewMvc.Listener> {
    interface Listener {
        fun onAnswerClicked(answer: Answer, viewMvc: IAnswersItemViewMvc)
    }
    fun updateTintColor(isCorrect: Boolean)
    fun bindAnswer(answer: Answer)
}