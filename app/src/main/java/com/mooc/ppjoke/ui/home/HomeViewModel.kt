package com.mooc.ppjoke.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.alibaba.fastjson.TypeReference
import com.mooc.libnetwork.ApiResponse
import com.mooc.libnetwork.ApiService
import com.mooc.libnetwork.JsonCallback
import com.mooc.libnetwork.request.Request
import com.mooc.ppjoke.R
import com.mooc.ppjoke.model.Feed
import com.mooc.ppjoke.ui.AbsViewModel
import com.mooc.ppjoke.ui.MutableDataSource
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

class HomeViewModel : AbsViewModel<Feed>() {

    val cacheLiveData = MutableLiveData<PagedList<Feed>>()
    private val shouldLoadAfter = AtomicBoolean(false)

    @Volatile
    private var withCache = true

    override fun createDataSource(): DataSource<Int, Feed> {
        return FeedDataSource()
    }

    inner class FeedDataSource : ItemKeyedDataSource<Int, Feed>() {
        override fun getKey(item: Feed): Int {
            return item.id
        }

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Feed>
        ) {
            loadData(0, params.requestedLoadSize, callback)
            withCache = false
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            loadData(params.key, params.requestedLoadSize, callback)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            callback.onResult(Collections.emptyList())
        }
    }

    fun loadData(key: Int, loadSize: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if(key>0){
            shouldLoadAfter.set(true)
        }

        // 构建request
        val request = ApiService.get<List<Feed>>("/feeds/queryHotFeedsList")
            .addParam("feedType", null)
            .addParam("userId", 0)
            .addParam("feedId", key)
            .addParam("pageCount", loadSize)
            .responseType(object : TypeReference<ArrayList<Feed>>() {}.type)

        // 如果需要缓存
        if (withCache) {
            // 仅仅缓存
            request.cacheStrategy(Request.CACHE_ONLY)
            request.execute(object : JsonCallback<List<Feed>>() {
                override fun onCacheSuccess(response: ApiResponse<List<Feed>>) {
                    Log.d("loadData", "onCacheSuccess: " + response.body?.size)
                    val dataSource = MutableDataSource<Int, Feed>()
                    response.body?.let {
                        dataSource.data.addAll(it)
                    }
                    val newPagedList = dataSource.buildNewPagedList(config)
                    cacheLiveData.postValue(newPagedList)
                }
            })
        }

        //如果需要网络
        val netRequest = if (withCache) {
            request.clone()
        } else {
            request
        }

        // 如果刷新,使用缓存,如果加载下一页数据,使用网络
        netRequest.cacheStrategy(
            if (key == 0) {
                Request.NET_CACHE
            } else {
                Request.NET_ONLY
            }
        )

        val response = netRequest.execute()
        val data = response?.body ?: emptyList()

        callback.onResult(data)
        // 如果是加载数据,则根据情况关闭页面
        if (key > 0) {
            boundaryPageData.postValue(data.isNotEmpty())
            shouldLoadAfter.set(false)
        }

        Log.d("loadData", "loadData: $key")
    }

    @SuppressLint("RestrictedApi")
    fun loadAfter(id: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if(shouldLoadAfter.get()){
            callback.onResult(Collections.emptyList())
            return
        }
        ArchTaskExecutor.getIOThreadExecutor().execute {
            loadData(id, config.pageSize, callback)
        }
    }
}