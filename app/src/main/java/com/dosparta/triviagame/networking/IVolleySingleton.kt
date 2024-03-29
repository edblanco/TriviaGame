package com.dosparta.triviagame.networking

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

interface IVolleySingleton {

    interface Listener{
        fun notifySuccess(response: String)
        fun notifyFailure(error: Exception?)
    }
    fun addStringRequestToQueue(requestMethod: Int, url: String, listener: Listener)
}