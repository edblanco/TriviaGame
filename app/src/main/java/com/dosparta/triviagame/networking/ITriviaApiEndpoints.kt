package com.dosparta.triviagame.networking

import android.net.Uri

interface ITriviaApiEndpoints {
    fun getQuestionsEndpoint(amount: String): String

    fun getQuestionsEndpoint(amount: String, category: Int): String
    fun getCategoriesEndpoint(): String
}