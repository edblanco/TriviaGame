package com.dosparta.triviagame.screens.common

import androidx.appcompat.app.AppCompatActivity
import com.dosparta.triviagame.TriviaGameApplication
import com.dosparta.triviagame.common.dependencyinjection.ControllerCompositionRoot

open class BaseActivity: AppCompatActivity() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        controllerCompositionRoot ?: ControllerCompositionRoot((application as TriviaGameApplication).getCompositionRoot(), this).also {
            controllerCompositionRoot = it
        }
        return controllerCompositionRoot!!
    }
}