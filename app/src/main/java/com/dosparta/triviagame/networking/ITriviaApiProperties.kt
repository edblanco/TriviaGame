package com.dosparta.triviagame.networking

interface ITriviaApiProperties {
    fun getScheme(): String
    fun getHost(): String
    fun getPath(): String
    fun getAmountParam(): String
    fun getCategoryPath(): String
    fun getCategoryParam(): String
}