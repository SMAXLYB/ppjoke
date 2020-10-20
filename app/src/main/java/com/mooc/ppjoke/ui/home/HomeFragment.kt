package com.mooc.ppjoke.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mooc.libnavannotation.FragmentDestination
import com.mooc.ppjoke.model.Feed
import com.mooc.ppjoke.ui.AbsListFragment
import com.mooc.ppjoke.ui.MutableDataSource
import com.scwang.smart.refresh.layout.api.RefreshLayout

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {


    companion object {
        fun newInstance(feedType: String): HomeFragment {
            val args = Bundle()
            args.putString("feedType", feedType)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

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