package com.dosparta.triviagame.questions

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
//import com.android.volley.utils.CacheTestUtils

class MockRequest : Request<ByteArray?> {
    constructor() : super(Method.GET, "http://foo.com", null) {}
    constructor(url: String?, listener: Response.ErrorListener?) : super(
        Method.GET, url, listener
    ) {
    }

    private var mPostParams: Map<String, String> = HashMap()
    fun setPostParams(postParams: Map<String, String>) {
        mPostParams = postParams
    }

    public override fun getPostParams(): Map<String, String>? {
        return mPostParams
    }

    private var mCacheKey = super.getCacheKey()
    fun setCacheKey(cacheKey: String) {
        mCacheKey = cacheKey
    }

    override fun getCacheKey(): String {
        return mCacheKey
    }

    var deliverResponse_called = false
    var parseResponse_called = false
    override fun deliverResponse(response: ByteArray?) {
        deliverResponse_called = true
    }

    var deliverError_called = false
    override fun deliverError(error: VolleyError) {
        super.deliverError(error)
        deliverError_called = true
    }

    var cancel_called = false
    override fun cancel() {
        cancel_called = true
        super.cancel()
    }

    private var mPriority = super.getPriority()
    fun setPriority(priority: Priority) {
        mPriority = priority
    }

    override fun getPriority(): Priority {
        return mPriority
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<ByteArray?> {
        parseResponse_called = true
        //return Response.success(response.data, CacheTestUtils.makeRandomCacheEntry(response.data))
        return Response.success(response.data, null)
    }
}