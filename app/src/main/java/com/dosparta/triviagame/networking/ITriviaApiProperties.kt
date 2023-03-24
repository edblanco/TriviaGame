package com.dosparta.triviagame.networking

interface ITriviaApiProperties {
    fun getScheme(): String
    fun getHost(): String
    fun getPath(): String
    fun getAmountParam(): String
}