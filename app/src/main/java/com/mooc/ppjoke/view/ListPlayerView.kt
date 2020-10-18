package com.mooc.ppjoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.mooc.libcommon.PixUtils
import com.mooc.ppjoke.R
import com.mooc.ppjoke.ui.home.ImageViewAdapter
import com.mooc.ppjoke.ui.home.setBlurImageUrl

class ListPlayerView : FrameLayout {
    private lateinit var bufferView: ProgressBar
    private lateinit var cover: ImageView
    private lateinit var blur: ImageView
    private lateinit var play: ImageView
    private lateinit var mCategory: String
    private lateinit var mVideoUrl: String

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true)
        bufferView = findViewById<ProgressBar>(R.id.buffer_view)
        cover = findViewById<ImageView>(R.id.cover)
        blur = findViewById<ImageView>(R.id.blur_background)
        play = findViewById<ImageView>(R.id.play)
    }

    /**
     * @param category 生命标志
     * @param widthPx 视频宽度
     * @param heightPx 视频高度
     * @param coverUrl 封面地址
     * @param videoUrl 视频地址
     */
    fun bindData(
        category: String,
        widthPx: Int,
        heightPx: Int,
        coverUrl: String,
        videoUrl: String
    ) {
        mCategory = category
        mVideoUrl = videoUrl

        // 加载封面
        ImageViewAdapter.setImageUrlAndShape(cover, coverUrl, false)

        // 加载高斯模糊背景
        // 仅在竖屏时显示高斯模糊
        if (widthPx < heightPx) {
            blur.setBlurImageUrl(coverUrl, 10)
            blur.visibility = VISIBLE
        } else {
            blur.visibility = INVISIBLE
        }

        // 给各组件设置宽高
        setSize(widthPx, heightPx)
    }

    /**
     * @param widthPx 视频宽度
     * @param heightPx 视频高度
     */
    private fun setSize(widthPx: Int, heightPx: Int) {
        // 组件最大宽高都为屏幕宽度
        val maxWidth = PixUtils.getScreenWidth()
        val maxHeight = maxWidth
        // 总布局宽高
        val layoutWidth = maxWidth
        var layoutHeight = 0
        // 封面宽高
        var coverWidth = 0
        var coverHeight = 0

        // 如果是竖屏
        if (widthPx < heightPx) {
            // 布局高度为最大
            layoutHeight = layoutWidth
            // 封面高度为最大
            coverHeight = layoutHeight
            // 封面宽度等比例缩放
            coverWidth = (widthPx / (heightPx * 1.0f / coverHeight)).toInt()
        } else {
            // 如果是横屏
            // 封面宽度
            coverWidth = layoutWidth
            // 封面高度,布局高度
            coverHeight = (heightPx / (widthPx * 1.0f / coverWidth)).toInt()
            layoutHeight = coverHeight
        }

        val layoutParams = this.layoutParams
        layoutParams.width = layoutWidth
        layoutParams.height = layoutHeight
        this.layoutParams = layoutParams

        val blurParams = blur.layoutParams
        blurParams.width = layoutWidth
        blurParams.height = layoutHeight
        blur.layoutParams = blurParams


        val coverParams = cover.layoutParams as LayoutParams
        coverParams.width = coverWidth
        coverParams.height = coverHeight
        coverParams.gravity = Gravity.CENTER
        cover.layoutParams = coverParams

        val playParams = play.layoutParams as LayoutParams
        playParams.gravity = Gravity.CENTER
        play.layoutParams = playParams
    }
}