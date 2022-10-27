package com.dosparta.triviagame.screens.trivia

import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener
import com.dosparta.triviagame.screens.common.ViewMvc

interface ITriviaGameViewMvc: ViewMvc {
    interface Listener{
        fun onAnswerClicked(isCorrect: Boolean)
    }
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun setLoadingState(loading: Boolean)
    fun bindQuestions(currentQuestion: Int, questions: List<Question>)
    fun showResults(correctAnswers: Int, totalAnswers: Int, answerListener: AlertDialogListener)
    fun showErrorDialog(statusCode: Int, answerListener: AlertDialogListener)
}
