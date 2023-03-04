package com.dosparta.triviagame.screens.common.dialogs

import com.dosparta.triviagame.common.BaseObservable

class DialogsEventBus : BaseObservable<DialogsEventBus.Listener>() {

    interface Listener {
        fun onDialogEvent(event: Any)
    }

    fun postEvent(event: Any) {
        for (listener in listeners) {
            listener.onDialogEvent(event)
        }
    }
}