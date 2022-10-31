package com.dosparta.triviagame.screens.trivia

import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener
import com.dosparta.triviagame.screens.common.ObservableViewMvc

interface ITriviaGameViewMvc: ObservableViewMvc<ITriviaGameViewMvc.Listener> {
    interface Listener{
        fun onAnswerClicked(isCorrect: Boolean)
    }
    fun setLoadingState(loading: Boolean)
    fun bindQuestions(currentQuestion: Int, questions: List<Question>)
    fun showResults(correctAnswers: Int, totalAnswers: Int, answerListener: AlertDialogListener) //todo move to message displayer
    fun showErrorDialog(statusCode: Int, answerListener: AlertDialogListener) //todo move to message displayer
}
