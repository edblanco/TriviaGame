package com.dosparta.triviagame.screens.common

import android.content.Context
import android.os.Handler

class ActivityUtils(context: Context) {

    private val handler: Handler

    init {
        handler = Handler(context.mainLooper)
    }

    fun postDelayed(runnable: Runnable, delay: Long) {
        handler.postDelayed(runnable, delay)
    }

    fun removeCallbacksAndMessages(token: Any?) {
        handler.removeCallbacksAndMessages(token)
    }
}