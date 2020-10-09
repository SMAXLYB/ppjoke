package com.mooc.libnetwork

// 不写成接口的原因是可以选择性的覆盖方法
abstract class JsonCallback<T> {
    fun onSuccess(response: ApiResponse<T>){}

    fun onError(response: ApiResponse<T>){}

    fun onCacheSuccess(response: ApiResponse<T>){}
}