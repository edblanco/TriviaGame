package com.dosparta.triviagame.questions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val category: String,
    val difficulty: String,
    val answers: List<Answer>,
    val question: String,
    val type: String
): Parcelable

@Parcelize
data class Answer (
    val answer: String,
    val correct: Boolean
    ): Parcelable
