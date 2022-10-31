package com.dosparta.triviagame.screens.common

import android.content.Context
import android.os.Handler

class ActivityUtils (private val context: Context) {
    fun postDelayed(runnable: Runnable, delay: Long) {
        Handler(context.mainLooper).postDelayed(runnable, delay)
    }
}