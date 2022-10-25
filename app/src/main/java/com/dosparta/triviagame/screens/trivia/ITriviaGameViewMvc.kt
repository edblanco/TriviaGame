package com.dosparta.triviagame.screens.trivia

import android.view.View
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener

interface ITriviaGameViewMvc {
    interface Listener{
        fun onAnswerClicked(isCorrect: Boolean)
    }
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun getRootView(): View
    fun setLoadingState(loading: Boolean)
    fun bindQuestions(currentQuestion: Int, questions: List<Question>)
    fun showResults(correctAnswers: Int, totalAnswers: Int, answerListener: AlertDialogListener)
    fun showErrorDialog(statusCode: Int, answerListener: AlertDialogListener)
}
