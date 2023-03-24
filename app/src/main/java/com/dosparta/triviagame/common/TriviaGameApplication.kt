package com.dosparta.triviagame.common

import android.app.Application
import com.dosparta.triviagame.common.dependencyinjection.CompositionRoot

class TriviaGameApplication : Application() {

    private var compositionRoot: CompositionRoot? = null

    override fun onCreate() {
        super.onCreate()
        compositionRoot = CompositionRoot()
    }

    fun getCompositionRoot(): CompositionRoot {
        return compositionRoot!!
    }
}