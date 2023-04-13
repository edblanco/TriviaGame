package com.dosparta.triviagame.networking.parsers

interface IHtmlParser {
    fun fromHtml(text: String, fromHtmlModeCompact: Int): String
}