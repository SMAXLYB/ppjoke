package com.mooc.ppjoke.ui

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.mooc.ppjoke.model.Feed
import java.util.*

class MutableDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {

    var data = arrayListOf<Value>()

    @SuppressLint("RestrictedApi")
    fun buildNewPagedList(config: PagedList.Config): PagedList<Value> {
        return PagedList.Builder<Key, Value>(this,config)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build()
    }

    override fun loadInitial(
        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Value>
    ) {
        callback.onResult(data, null, null)
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(Collections.emptyList(), null)
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(Collections.emptyList(), null)
    }
}