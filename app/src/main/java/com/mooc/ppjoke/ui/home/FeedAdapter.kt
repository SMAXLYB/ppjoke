package com.mooc.ppjoke.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mooc.libcommon.PixUtils
import com.mooc.ppjoke.databinding.LayoutFeedInteractionBinding
import com.mooc.ppjoke.databinding.LayoutFeedTypeImageBinding
import com.mooc.ppjoke.databinding.LayoutFeedTypeVideoBinding
import com.mooc.ppjoke.model.Feed

class FeedAdapter(val context: Context, val category: String) :
    PagedListAdapter<Feed, FeedAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val feed = getItem(position) as Feed
        return feed.itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding = if (viewType == Feed.TYPE_IMAGE) {
            LayoutFeedTypeImageBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            LayoutFeedTypeVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        }

        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position)?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(view: View, private val mBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(view) {

        // 绑定数据dataBinding
        fun bindData(feed: Feed) {
            // 分布局
            if (mBinding is LayoutFeedTypeImageBinding) {
                mBinding as LayoutFeedTypeImageBinding
                mBinding.feed = feed
                mBinding.feedImage.bindData(
                    feed.width,
                    feed.height,
                    16,
                    PixUtils.getScreenWidth(),
                    PixUtils.getScreenHeight(),
                    feed.cover
                )
            } else {
                mBinding as LayoutFeedTypeVideoBinding
                mBinding.feed = feed
                mBinding.listPlayerView.bindData(category,feed.width,feed.height,feed.cover,feed.url)
            }
        }
    }
}