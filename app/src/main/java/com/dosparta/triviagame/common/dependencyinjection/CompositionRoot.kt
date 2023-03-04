package com.dosparta.triviagame.common.dependencyinjection

import android.content.Context
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

class CompositionRoot {

    private var volley: VolleySingleton? = null
    private var dialogsEventBus: DialogsEventBus? = null

    fun getVolleyInstance(context: Context): VolleySingleton {
        return getVolley(context.applicationContext)
    }

    private fun getVolley(context: Context): VolleySingleton {
        volley ?: VolleySingleton(context.applicationContext).also {
            volley = it
        }
        return volley!!
    }

    fun getDialogsEventBus(): DialogsEventBus {
        dialogsEventBus ?: DialogsEventBus().also {
            dialogsEventBus = it
        }
        return dialogsEventBus!!
    }
}