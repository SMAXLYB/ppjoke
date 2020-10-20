package com.mooc.ppjoke.ui.publish

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mooc.libcommon.PixUtils
import com.mooc.libcommon.PixUtils.Companion.getScreenHeight
import com.mooc.libcommon.dp
import com.mooc.libnetwork.ApiResponse
import com.mooc.libnetwork.ApiService
import com.mooc.libnetwork.JsonCallback
import com.mooc.ppjoke.R
import com.mooc.ppjoke.model.TagList
import com.mooc.ppjoke.ui.login.UserManager


class TagBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagsAdapter: TagsAdapter
    private val mTagLists = arrayListOf<TagList>()
    private var listener: OnTagItemSelectedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_tag_bottom_sheet_dialog, null, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        tagsAdapter = TagsAdapter()
        recyclerView.adapter = tagsAdapter

        dialog.setContentView(view)
        val parent = view.parent as ViewGroup
        val behavior = BottomSheetBehavior.from(parent)
        behavior.peekHeight = PixUtils.getScreenHeight() / 3
        behavior.isHideable = false

        val layoutParams: ViewGroup.LayoutParams = parent.layoutParams
        layoutParams.height = getScreenHeight() / 3 * 2
        parent.layoutParams = layoutParams

        queryTagList();

        return dialog
    }

    private fun queryTagList() {
        ApiService.get<List<TagList>>("/tag/queryTagList")
            .addParam("userId", UserManager.getUserId())
            .addParam("pageCount", 100)
            .addParam("tagId", 0).execute(object : JsonCallback<List<TagList>>() {
                @SuppressLint("RestrictedApi")
                override fun onSuccess(response: ApiResponse<List<TagList>>) {
                    super.onSuccess(response)
                    if (response.body != null) {
                        val list = response.body!!
                        mTagLists.addAll(list)
                        ArchTaskExecutor.getMainThreadExecutor()
                            .execute(Runnable { tagsAdapter.notifyDataSetChanged() })
                    }
                }

                @SuppressLint("RestrictedApi")
                override fun onError(response: ApiResponse<List<TagList>>) {
                    ArchTaskExecutor.getMainThreadExecutor().execute(Runnable {
                        Toast.makeText(
                            context,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            })
    }

    inner class TagsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val textView = TextView(parent.context)
            textView.textSize = 13F
            textView.setTypeface(Typeface.DEFAULT_BOLD)
            textView.setTextColor(ContextCompat.getColor(parent.context, R.color.color_000))
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.layoutParams = RecyclerView.LayoutParams(-1, 45.toFloat().dp.toInt())
            return object : RecyclerView.ViewHolder(textView) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val textView = holder.itemView as TextView
            val tagList: TagList = mTagLists[position]
            textView.text = tagList.title
            holder.itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onTagItemSelected(tagList)
                    dismiss()
                }
            }
        }

        override fun getItemCount(): Int {
            return mTagLists.size
        }
    }

    fun setOnTagItemSelectedListener(listener: OnTagItemSelectedListener) {
        this.listener = listener
    }

    interface OnTagItemSelectedListener {
        fun onTagItemSelected(tagList: TagList)
    }
}