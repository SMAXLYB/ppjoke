package com.mooc.ppjoke.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mooc.libcommon.EmptyView
import com.mooc.ppjoke.R
import com.mooc.ppjoke.databinding.LayoutRefreshViewBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import java.lang.reflect.ParameterizedType


abstract class AbsListFragment<T, M : AbsViewModel<T>> : Fragment(), OnRefreshListener,
    OnLoadMoreListener {
    private lateinit var binding: LayoutRefreshViewBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefreshLayout: SmartRefreshLayout
    protected lateinit var mAdapter: PagedListAdapter<T, out RecyclerView.ViewHolder>
    protected lateinit var mViewModel: M

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutRefreshViewBinding.inflate(layoutInflater, container, false)

        mRecyclerView = binding.recyclerView
        mRefreshLayout = binding.refreshLayout

        mRefreshLayout.setEnableLoadMore(true)
        mRefreshLayout.setEnableRefresh(true)
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setOnLoadMoreListener(this)

        mAdapter = getAdapter()

        mRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.itemAnimator = null
        mRecyclerView.adapter = mAdapter
        val decoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.list_divider
            )!!
        )
        mRecyclerView.addItemDecoration(decoration)

        genericViewModel()

        return binding.root
    }

    private fun genericViewModel(){
        val type = this.javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if (arguments.size > 1) {
            val argument = arguments[1]
            val modelClazz = (argument as Class<*>).asSubclass(
                AbsViewModel::class.java
            ) as Class<AbsViewModel<T>>

            mViewModel = ViewModelProvider(this).get(modelClazz) as M

            mViewModel.pageData.observe(viewLifecycleOwner) {
                submitList(it)
            }

            // 控制空布局的显示与隐藏
            mViewModel.boundaryPageData.observe(viewLifecycleOwner) {
                finishRefresh(it)
            }
        }
    }

    protected fun submitList(pagedList: PagedList<T>) {
        // 如果有数据,交给adapter
        if (pagedList.size > 0) {
            mAdapter.submitList(pagedList)
        }

        finishRefresh(pagedList.size > 0)
    }

    private fun finishRefresh(hasData: Boolean) {
        val currentList = mAdapter.currentList as PagedList<T>?

        val mHasData = hasData || currentList != null && currentList.size > 0

        // 停止刷新
        val state = mRefreshLayout.state
        if (state.isFooter && state.isOpening) {
            mRefreshLayout.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            mRefreshLayout.finishRefresh()
        }
    }

    // 子类实现
    abstract fun getAdapter(): PagedListAdapter<T, out RecyclerView.ViewHolder>
}