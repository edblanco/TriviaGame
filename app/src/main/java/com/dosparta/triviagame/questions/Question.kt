package com.dosparta.triviagame.questions

data class Question(
    val category: String,
    val difficulty: String,
    val answers: List<Answer>,
    val question: String,
    val type: String
)

data class Answer (
    val answer: String,
    val correct: Boolean
    )
