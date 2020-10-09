package com.mooc.libnetwork.utils

import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLEncoder

class UrlCreator {
    companion object {
        @JvmStatic
        fun createUrlFromParams(url: String, params: Map<String, Any>): String {
            val builder = StringBuilder()

            return with(builder) {
                append(url)

                // 如果url已经带了参数,拼接的时候就不需要携带?
                if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
                    append("&")
                } else {
                    append("?")
                }

                // 对参数进行拼接
                params.forEach {
                    // 对值进行编码
                    try {
                        val value = URLEncoder.encode(it.value.toString(), "UTF-8")
                        append(it.key).append("=").append(value).append("&")
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }

                deleteCharAt(length - 1)
            }.toString()
        }
    }
}