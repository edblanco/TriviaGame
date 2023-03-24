package com.dosparta.triviagame.networking

import android.net.Uri

interface ITriviaApiEndpoints {
    fun getQuestionsEndpoint(amount: String): Uri?
}