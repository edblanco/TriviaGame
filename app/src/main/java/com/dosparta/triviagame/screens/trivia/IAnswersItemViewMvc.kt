package com.dosparta.triviagame.screens.trivia

import android.view.View
import com.dosparta.triviagame.questions.Answer

interface IAnswersItemViewMvc {
    interface Listener {
        fun onAnswerClicked(answer: Answer, holder: AnswersRecyclerAdapter.ViewHolder)
    }
    fun registerListener(listener: Listener)
    fun unregisterListener(listener: Listener)
    fun getRootView() : View
    fun updateTintColor(holder: AnswersRecyclerAdapter.ViewHolder, isCorrect: Boolean)
    fun bindAnswer(answer: Answer, holder: AnswersRecyclerAdapter.ViewHolder)
}