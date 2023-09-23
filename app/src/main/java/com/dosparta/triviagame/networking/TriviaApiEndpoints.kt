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

    override fun getQuestionsEndpoint(amount: String, category: Int): String {
        val urlBuilder = Uri.Builder()
        return urlBuilder.scheme(triviaApiProperties.getScheme())
            .authority(triviaApiProperties.getHost()).path(triviaApiProperties.getPath())
            .appendQueryParameter(triviaApiProperties.getAmountParam(), amount)
            .appendQueryParameter(triviaApiProperties.getCategoryParam(), category.toString())
            .build().toString()
    }

    override fun getCategoriesEndpoint(): String {
        val urlBuilder = Uri.Builder()
        return urlBuilder.scheme(triviaApiProperties.getScheme())
            .authority(triviaApiProperties.getHost())
            .path(triviaApiProperties.getCategoryPath())
            .build().toString()
    }
}