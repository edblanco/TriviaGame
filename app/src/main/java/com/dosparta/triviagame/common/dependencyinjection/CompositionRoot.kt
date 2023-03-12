package com.dosparta.triviagame.common.dependencyinjection

import android.content.Context
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

class CompositionRoot {

    private var volley: IVolleySingleton? = null
    private var dialogsEventBus: DialogsEventBus? = null

    fun getVolleyInstance(context: Context): IVolleySingleton {
        return getVolley(context.applicationContext)
    }

    private fun getVolley(context: Context): IVolleySingleton {
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