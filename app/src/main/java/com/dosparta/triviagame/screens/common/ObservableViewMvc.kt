package com.dosparta.triviagame.screens.common

interface ObservableViewMvc<ListenerType>: ViewMvc {
    fun registerListener(listener: ListenerType)
    fun unregisterListener(listener: ListenerType)
}