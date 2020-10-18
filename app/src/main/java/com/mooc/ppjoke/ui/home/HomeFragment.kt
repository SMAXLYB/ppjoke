package com.mooc.ppjoke.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mooc.libnavannotation.FragmentDestination
import com.mooc.ppjoke.R
import com.mooc.ppjoke.model.Feed
import com.mooc.ppjoke.ui.AbsListFragment
import com.mooc.ppjoke.ui.MutableDataSource
import com.scwang.smart.refresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {

    override fun getAdapter(): PagedListAdapter<Feed, out RecyclerView.ViewHolder> {
        val feedType = if (arguments != null) {
            arguments?.getString("feedType")
        } else {
            "all"
        }
        return FeedAdapter(requireContext(), feedType!!)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        // 刷新数据
        mViewModel.dataSource.invalidate()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val feed = mAdapter.currentList?.get(mAdapter.itemCount - 1)
        feed?.id?.let {
            mViewModel.loadAfter(it, object : ItemKeyedDataSource.LoadCallback<Feed>() {
                override fun onResult(data: MutableList<Feed>) {
                    if (data.size > 0) {
                        val dataSource = MutableDataSource<Int, Feed>()
                        dataSource.data.addAll(data)
                        mAdapter.currentList?.config?.let { config ->
                            val newPagedList = dataSource.buildNewPagedList(
                                config
                            )
                            submitList(newPagedList)
                        }
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel.cacheLiveData.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }
}