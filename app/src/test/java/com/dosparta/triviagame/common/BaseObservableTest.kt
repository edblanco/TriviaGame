package com.dosparta.triviagame.common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

internal class BaseObservableTest {

    private lateinit var SUT: BaseObservableSUT
    private lateinit var listener: Listener
    private lateinit var listener2: Listener

    @BeforeEach
    internal fun setup() {
        SUT = BaseObservableSUT()
        listener = Listener()
        listener2 = Listener()
    }

    @Test
    fun registerListener_oneListener_containsListener() {
        SUT.registerListener(listener)
        val listeners = SUT.getListenersTest()
        assertTrue(listeners.size == 1)
        assertTrue(listeners.contains(listener))
    }

    @Test
    fun registerListener_twoListener_containsListener() {
        SUT.registerListener(listener)
        SUT.registerListener(listener2)
        val listeners = SUT.getListenersTest()
        assertTrue(listeners.size == 2)
        assertTrue(listeners.contains(listener))
        assertTrue(listeners.contains(listener2))
    }

    @Test
    fun unregisterListener_oneListener() {
        SUT.registerListener(listener)
        SUT.unregisterListener(listener)
        val listeners = SUT.getListenersTest()

        assertTrue(listeners.isEmpty())
    }

    @Test
    fun unregisterListener_twoListenerUnregisterOne() {
        SUT.registerListener(listener)
        SUT.registerListener(listener2)
        SUT.unregisterListener(listener)
        val listeners = SUT.getListenersTest()

        assertTrue(listeners.size == 1)
        assertTrue(listeners.contains(listener2))
    }

    @Test
    fun unregisterListener_twoListenerUnregisterAll() {
        SUT.registerListener(listener)
        SUT.registerListener(listener2)
        SUT.unregisterListener(listener)
        SUT.unregisterListener(listener2)
        val listeners = SUT.getListenersTest()

        assertTrue(listeners.isEmpty())
    }

    @Test
    fun unregisterListener_unregisterNotRegisterListener() {
        SUT.registerListener(listener)
        SUT.unregisterListener(listener)
        SUT.unregisterListener(listener2)
        val listeners = SUT.getListenersTest()

        assertTrue(listeners.isEmpty())
    }

    private class Listener

    private class BaseObservableSUT: BaseObservable<Listener>() {
        fun getListenersTest(): Set<Listener> {
            return listeners
        }
    }
}