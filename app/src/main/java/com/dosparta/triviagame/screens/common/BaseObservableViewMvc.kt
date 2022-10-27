package com.dosparta.triviagame.screens.common

import java.util.*

abstract class BaseObservableViewMvc<ListenerType> : BaseViewMvc(), ObservableViewMvc<ListenerType> {
    private val listeners: MutableSet<ListenerType> = mutableSetOf()

    override fun registerListener(listener: ListenerType) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: ListenerType) {
        listeners.remove(listener)
    }

    protected fun getListeners(): Set<ListenerType> {
        return Collections.unmodifiableSet(listeners)
    }
}