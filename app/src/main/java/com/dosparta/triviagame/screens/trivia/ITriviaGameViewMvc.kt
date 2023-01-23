package com.dosparta.triviagame.screens.trivia

import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.views.ObservableViewMvc
import com.dosparta.triviagame.screens.trivia.answersitem.IAnswersItemViewMvc

interface ITriviaGameViewMvc: ObservableViewMvc<ITriviaGameViewMvc.Listener> {
    interface Listener{
        fun onAnswerClicked(answer: Answer, answersViewMvc: IAnswersItemViewMvc)
        fun onButtonNextClicked()
        fun onCorrectAnswerFound(answersItemViewMvc: IAnswersItemViewMvc)
    }
    fun setLoadingState(loading: Boolean)
    fun bindQuestions(currentQuestion: Int, questions: List<Question>)
    fun showButtonNext(show: Boolean)
    fun updateCorrectQuestion()
}
