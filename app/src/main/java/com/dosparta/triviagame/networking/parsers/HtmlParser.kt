package com.dosparta.triviagame.networking.parsers

import android.text.Html

class HtmlParser : IHtmlParser {
    override fun fromHtml(text: String, fromHtmlModeCompact: Int): String {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString()
    }
}