package com.mooc.libnetwork.request

import okhttp3.FormBody

class PostRequest<T>(url: String) : Request<T, PostRequest<T>>(url) {

    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        val bodyBuilder = FormBody.Builder()
        params.forEach {
            bodyBuilder.add(it.key, it.value.toString())
        }

        return builder.url(mUrl)
            .post(bodyBuilder.build())
            .build()
    }
}