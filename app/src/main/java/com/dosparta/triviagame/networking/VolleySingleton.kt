package com.dosparta.triviagame.networking

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class VolleySingleton constructor(context: Context) : IVolleySingleton {
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(20)
                override fun getBitmap(url: String): Bitmap {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    private fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    override fun addStringRequestToQueue(requestMethod: Int, url: String, listener: IVolleySingleton.Listener) {
        addToRequestQueue(createStringRequest(requestMethod, url, listener))
    }

    private fun createStringRequest(requestMethod: Int, url: String, listener: IVolleySingleton.Listener): StringRequest {
        return StringRequest(requestMethod, url, { response ->
            listener.notifySuccess(response)
        }, {
            listener.notifyFailure(it)
        })
    }
}