package com.dosparta.triviagame

import android.app.Application

class TriviaGameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VolleySingleton.getInstance(this)
    }
}