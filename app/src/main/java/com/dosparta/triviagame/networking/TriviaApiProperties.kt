package com.dosparta.triviagame.networking

import android.content.Context
import com.dosparta.triviagame.common.Utils

class TriviaApiProperties(context: Context) : ITriviaApiProperties {
    private val properties = Utils.getProperty("trivia_api.properties", context)

    override fun getScheme(): String {
        return properties.getProperty("scheme")
    }

    override fun getHost(): String {
        return properties.getProperty("host")
    }

    override fun getPath(): String {
        return properties.getProperty("path")
    }

    override fun getAmountParam(): String {
        return properties.getProperty("amount_param")
    }
}