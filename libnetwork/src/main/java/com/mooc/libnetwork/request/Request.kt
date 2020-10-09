package com.mooc.libnetwork.request

import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import com.mooc.libnetwork.ApiResponse
import com.mooc.libnetwork.ApiService
import com.mooc.libnetwork.JsonCallback
// import com.mooc.libnetwork.cache.CacheManager
import com.mooc.libnetwork.utils.UrlCreator
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.Serializable
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class Request<T, R : Request<T, R>>(protected val mUrl: String) {

    protected val headers = HashMap<String, String>()
    protected val params = HashMap<String, Any>()
    private var mCacheKey: String? = null
    private var mType: Type? = null
    private var mCacheStrategy: Int = 0

    companion object {
        // 仅仅使用缓存,即使缓存不存在
        const val CACHE_ONLY = 1

        // 先访问缓存,同时访问网络请求,然后缓存到本地
        const val CACHE_FIRST = 2

        // 仅仅发起网络请求,不缓存
        const val NET_ONLY = 3

        // 先发起网络请求,然后缓存到本地
        const val NET_CACHE = 4
    }


    @IntDef(value = [CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE])
    annotation class CacheStrategy

    // 使用者添加headers
    fun addHeader(key: String, value: String): R {
        headers[key] = value
        return this as R
    }

    // 使用者添加请求参数
    fun addParam(key: String, value: Any): R {
        val field = value.javaClass.getField("TYPE")
        val clazz = field.get(null) as Class<*>
        if (clazz.isPrimitive) {
            params[key] = value
        }
        return this as R
    }

    // 使用者决定是否缓存等
    fun cacheStrategy(@CacheStrategy cacheStrategy: Int): R {
        mCacheStrategy = cacheStrategy
        return this as R
    }

    // 缓存对象的键
    fun cacheKey(key: String): R {
        mCacheKey = key
        return this as R
    }

    // 使用者设置实体转换对象的类型
    fun responseType(type: Type): R {
        mType = type
        return this as R
    }

    // 使用者发起请求
    fun execute(callback: JsonCallback<T>) {

        getCall().enqueue(object : Callback {

            // 本地网络发生了异常
            override fun onFailure(call: Call, e: IOException) {
                val response = ApiResponse<T>()
                response.message = e.message
                callback.onError(response)
            }

            // 服务器端响应
            override fun onResponse(call: Call, response: Response) {
                // 解析响应结果
                val apiResponse: ApiResponse<T> = parseResponse(response, callback)

                // 即使服务器响应成功,也还要判断响应的内容结果是否成功
                if (!apiResponse.success) {
                    callback.onError(apiResponse)
                } else {
                    callback.onSuccess(apiResponse)
                }
            }
        })
    }

    fun execute(): ApiResponse<T>? {
        try {
            val response = getCall().execute()
            return parseResponse(response, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // 解析服务器端响应,可能404,也可能200
    private fun parseResponse(response: Response, callback: JsonCallback<T>?): ApiResponse<T> {
        var message: String? = null
        val result = ApiResponse<T>()

        val status = response.code
        var isSuccess = response.isSuccessful
        val converter = ApiService.sConverter

        try {
            // 获取响应文本
            val content = response.body?.string() as String

            // 如果200
            if (isSuccess) {
                if (callback != null) {
                    // 获取泛型父类
                    val type = callback.javaClass.genericSuperclass as ParameterizedType
                    // 获取泛型类型
                    val argument = type.actualTypeArguments[0]
                    // 将文本内容进行转换为对象
                    result.body = converter.convert(content, argument) as T
                } else if (mType != null) {
                    result.body = converter.convert(content, mType!!) as T
                } else {
                    Log.e("Request", "parseResponse: 未传入callback或者type为空")
                }
            } else {
                // 如果404
                message = content
            }
        } catch (e: Exception) {
            message = e.message
            isSuccess = false
        }

        with(result) {
            this.status = status
            this.success = isSuccess
            this.message = message
        }

        // 进行缓存
        if (mCacheStrategy != NET_ONLY
            && result.success
            && result.body != null
            && result.body is Serializable
        ) {
            saveCache(result.body as T)
        }

        return result
    }

    private fun saveCache(body: T) {
        val key =
            if (TextUtils.isEmpty(mCacheKey)) {
                generateCacheKey()
            } else {
                mCacheKey!!
            }
        // CacheManager.save(key,body)
    }

    private fun generateCacheKey(): String {
        mCacheKey = UrlCreator.createUrlFromParams(mUrl, params)
        return mCacheKey!!
    }

    // 内部将headers添加到request中
    private fun getCall(): Call {
        val builder = okhttp3.Request.Builder().also {
            addHeaders(it)
        }
        val request = generateRequest(builder)
        return ApiService.OkHttpHolder.OK_HTTP_CLIENT.newCall(request)
    }

    // 由子类实现,决定请求方式
    protected abstract fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request

    private fun addHeaders(builder: okhttp3.Request.Builder) {
        headers.forEach {
            builder.addHeader(it.key, it.value)
        }
    }
}