package com.dosparta.triviagame.networking

import android.net.Uri

class TriviaApiEndpoints(private val triviaApiProperties: ITriviaApiProperties) :
    ITriviaApiEndpoints {

    override fun getQuestionsEndpoint(amount: String): String {
        val urlBuilder = Uri.Builder()
        return urlBuilder.scheme(triviaApiProperties.getScheme())
            .authority(triviaApiProperties.getHost()).path(triviaApiProperties.getPath())
            .appendQueryParameter(triviaApiProperties.getAmountParam(), amount).build().toString()
    }
}