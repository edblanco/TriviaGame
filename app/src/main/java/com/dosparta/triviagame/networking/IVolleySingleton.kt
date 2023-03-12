package com.dosparta.triviagame.networking

import com.android.volley.Request

interface IVolleySingleton {
    fun <T> addToRequestQueue(req: Request<T>)
}