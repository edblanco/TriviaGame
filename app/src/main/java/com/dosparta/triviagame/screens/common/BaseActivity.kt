package com.dosparta.triviagame.screens.common

import androidx.appcompat.app.AppCompatActivity
import com.dosparta.triviagame.TriviaGameApplication
import com.dosparta.triviagame.common.dependencyinjection.CompositionRoot

open class BaseActivity: AppCompatActivity() {

    protected fun getCompositionRoot(): CompositionRoot {
        return (application as TriviaGameApplication).getCompositionRoot()
    }
}