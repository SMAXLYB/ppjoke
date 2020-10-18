package com.mooc.libnetwork

import com.mooc.libnetwork.converter.Converter
import com.mooc.libnetwork.converter.JsonConverter
import com.mooc.libnetwork.request.GetRequest
import com.mooc.libnetwork.request.PostRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiService {
    init {
        val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf<X509Certificate>()
            }
        })

        val ssl: SSLContext =
            SSLContext.getInstance("SSL")
        ssl.init(null, trustManagers, SecureRandom())

        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier(HostnameVerifier { _, _ -> true })
    }

    var sBaseUrl: String? = null
    var sConverter: Converter<*> = JsonConverter()

    object OkHttpHolder {

        // 打印网络请求日志
        private val interceptor: HttpLoggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        val OK_HTTP_CLIENT = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    fun initiate(baseUrl: String, converter: Converter<*>?) {
        sBaseUrl = baseUrl
        converter?.let {
            sConverter = converter
        }
    }

    fun <T> get(url: String): GetRequest<T> {
        return GetRequest<T>(sBaseUrl + url)
    }

    fun <T> post(url: String): PostRequest<T> {
        return PostRequest<T>(sBaseUrl + url)
    }
}