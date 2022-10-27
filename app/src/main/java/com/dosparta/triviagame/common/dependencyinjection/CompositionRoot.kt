package com.dosparta.triviagame.common.dependencyinjection

import android.content.Context
import com.dosparta.triviagame.networking.VolleySingleton

class CompositionRoot {

    private var volley: VolleySingleton? = null

    fun getVolleyInstance(context: Context): VolleySingleton {
        return getVolley(context.applicationContext)
    }

    private fun getVolley(context: Context): VolleySingleton {
        volley ?: VolleySingleton(context.applicationContext).also {
            volley = it
        }
        return volley!!
    }
}