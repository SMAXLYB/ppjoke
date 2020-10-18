package com.mooc.libnetwork

// 不写成接口的原因是可以选择性的覆盖方法
abstract class JsonCallback<T> {
    open fun onSuccess(response: ApiResponse<T>){}

    open fun onError(response: ApiResponse<T>){}

    open fun onCacheSuccess(response: ApiResponse<T>){}
}