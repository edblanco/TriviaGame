package com.dosparta.triviagame.screens.common.dialogs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.kotlin.firstValue
import org.mockito.kotlin.secondValue
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

internal class DialogsEventBusTest {

    private lateinit var classUnderText: DialogsEventBus
    private lateinit var event1: Any
    private lateinit var event2: Any
    private lateinit var listener1: DialogsEventBus.Listener
    private lateinit var listener2: DialogsEventBus.Listener

    @BeforeEach
    internal fun setup() {
        classUnderText = DialogsEventBus()
        event1 = Any()
        event2 = Any()
        listener1 = Mockito.mock(DialogsEventBus.Listener::class.java)
        listener2 = Mockito.mock(DialogsEventBus.Listener::class.java)
        classUnderText.registerListener(listener1)
        classUnderText.registerListener(listener2)
    }

    @Test
    fun postEvent_onDialogEvent_sameObject() {
        val captor: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)
        val captor2: ArgumentCaptor<Any> = ArgumentCaptor.forClass(Any::class.java)

        classUnderText.postEvent(event1)
        classUnderText.postEvent(event2)

        verify(listener1, times(2)).onDialogEvent(capture(captor))
        verify(listener2, times(2)).onDialogEvent(capture(captor2))

        assertEquals(event1, captor.firstValue)
        assertEquals(event2, captor.secondValue)
        assertEquals(event1, captor2.firstValue)
        assertEquals(event2, captor2.secondValue)
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

}