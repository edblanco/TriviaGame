package com.dosparta.triviagame

import kotlinx.serialization.Serializable

@Serializable
data class QuestionsSchema(
    val response_code: Int,
    val results: List<Result>
)

@Serializable
data class Result(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
)