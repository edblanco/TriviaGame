package com.dosparta.triviagame

import android.app.Application
import com.dosparta.triviagame.networking.VolleySingleton

class TriviaGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VolleySingleton.getInstance(this)
    }
}