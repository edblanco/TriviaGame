package com.dosparta.triviagame.networking

import android.content.Context
import com.dosparta.triviagame.common.Utils

private const val SCHEME = "scheme"
private const val HOST = "host"
private const val PATH = "path"
private const val AMOUNT_PARAM = "amount_param"
private const val CATEGORY_PARAM = "category_param"
private const val CATEGORY_PATH = "category_path"

class TriviaApiProperties(context: Context) : ITriviaApiProperties {
    private val properties = Utils.getProperty("trivia_api.properties", context)

    override fun getScheme(): String {
        return getProperty(SCHEME)
    }

    override fun getHost(): String {
        return getProperty(HOST)
    }

    override fun getPath(): String {
        return getProperty(PATH)
    }

    override fun getAmountParam(): String {
        return getProperty(AMOUNT_PARAM)
    }

    override fun getCategoryPath(): String {
        return getProperty(CATEGORY_PATH)
    }

    override fun getCategoryParam(): String {
        return getProperty(CATEGORY_PARAM)
    }

    private fun getProperty(id: String): String {
        return properties.getProperty(id)
    }
}