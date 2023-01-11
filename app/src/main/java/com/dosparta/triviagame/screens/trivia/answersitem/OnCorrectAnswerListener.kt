package com.dosparta.triviagame.screens.trivia.answersitem

import com.dosparta.triviagame.questions.Answer

interface OnCorrectAnswerListener{
    fun onAnswerClicked(answer: Answer, answersViewMvc: IAnswersItemViewMvc)
    fun onCorrectAnswerFound(answersItemViewMvc: IAnswersItemViewMvc)
}