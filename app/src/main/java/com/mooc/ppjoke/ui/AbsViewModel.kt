package com.mooc.ppjoke.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

abstract class AbsViewModel<T> : ViewModel() {
    lateinit var dataSource: DataSource<Int, T>
    lateinit var pageData: LiveData<PagedList<T>>
    val boundaryPageData = MutableLiveData<Boolean>()
    protected var config : PagedList.Config = PagedList.Config.Builder()
         .setPageSize(2)
         .setInitialLoadSizeHint(3)
         .setEnablePlaceholders(false)
         .build()

    private val factory = object : DataSource.Factory<Int, T>() {
        override fun create(): DataSource<Int, T> {
            dataSource = createDataSource()
            return dataSource
        }
    }

    private val callback = object : PagedList.BoundaryCallback<T>() {
        override fun onZeroItemsLoaded() {
            boundaryPageData.postValue(false)
        }

        override fun onItemAtFrontLoaded(itemAtFront: T) {
            boundaryPageData.postValue(true)
        }

        override fun onItemAtEndLoaded(itemAtEnd: T) {
            super.onItemAtEndLoaded(itemAtEnd)
        }
    }

    init {

        pageData = LivePagedListBuilder(factory, config)
            .setInitialLoadKey(0)
            .setBoundaryCallback(callback)
            .build()
    }

    abstract fun createDataSource(): DataSource<Int, T>
}