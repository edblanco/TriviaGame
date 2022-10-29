package com.dosparta.triviagame.common.dependencyinjection

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.screens.common.ViewMvcFactory

class ControllerCompositionRoot(private val compositionRoot: CompositionRoot, private val activity: Activity) {

    fun getVolleyInstance(context: Context): VolleySingleton {
        return compositionRoot.getVolleyInstance(context)
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }
}