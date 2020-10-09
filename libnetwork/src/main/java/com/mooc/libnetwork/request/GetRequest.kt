package com.mooc.libnetwork.request

import com.mooc.libnetwork.utils.UrlCreator

class GetRequest<T>(url: String) : Request<T, GetRequest<T>>(url) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        return builder.url(UrlCreator.createUrlFromParams(mUrl, params)).build()
    }
}