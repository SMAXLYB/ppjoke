package com.mooc.ppjoke.ui.home

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.mooc.libcommon.PixUtils
import com.mooc.libcommon.dp
import com.mooc.ppjoke.ui.home.ImageViewAdapter.Companion.setImageUrlAndShape
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.layout_player_view.view.*

class ImageViewAdapter {
    companion object {

        @JvmStatic
        @BindingAdapter(value = ["image_url", "isCircle"], requireAll = false)
        fun setImageUrlAndShape(
            imageView: ImageView,
            imageUrl: String?,
            isCircle: Boolean?
        ) {
            val mIsCircle = isCircle ?: false

            //　如果设置了图片地址
            imageUrl?.let {
                val builder = Glide.with(imageView).load(imageUrl)
                // 设置剪裁
                if (mIsCircle) {
                    builder.transform(CircleCrop())
                }

                // 按照view的大小设置图片
                val layoutParams = imageView.layoutParams
                layoutParams?.let {
                    if (it.width > 0 && it.height > 0) {
                        builder.override(it.width, it.height)
                    }
                }

                builder.into(imageView)
            }
        }
    }
}

/**
 * @param widthPx 图片宽度 由服务器返回
 * @param heightPx 图片高度 由服务器返回
 * @param marginLeft 图片边距
 * @param maxWidth view最大宽度
 * @param maxHeight view最大高度
 * @param imageUrl 图片地址
 */
fun ImageView.bindData(
    widthPx: Int,
    heightPx: Int,
    marginLeft: Int,
    maxWidth: Int,
    maxHeight: Int,
    imageUrl: String
) {
    // 如果服务器未返回图片大小,需要自行获取
    if (widthPx <= 0 || heightPx <= 0) {
        Glide.with(this).load(imageUrl).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                // 获取图片宽高
                val width = resource.intrinsicWidth
                val height = resource.intrinsicHeight
                // 设置图片最终大小
                setSize(width, height, marginLeft, maxWidth, maxHeight)
                setImageDrawable(resource)
            }
        })
        return
    }

    // 如果服务器返回了图片的大小,直接设置图片大小
    setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight)
    setImageUrlAndShape(this, imageUrl, false)
}

// 设置图片大小
fun ImageView.setSize(
    width: Int,
    height: Int,
    marginLeft: Int,
    maxWidth: Int,
    maxHeight: Int
) {
    var finalWidth = 0
    var finalHeight = 0
    // 如果是横图
    if (width > height) {
        finalWidth = maxWidth
        finalHeight = (height / (width * 1.0f / finalWidth)).toInt()
    } else {
        // 如果是竖图
        finalHeight = maxHeight
        finalWidth = (width / (height * 1.0f / finalHeight)).toInt()
    }

    val params = layoutParams
    params.width = finalWidth
    params.height = finalHeight

    if (params is FrameLayout.LayoutParams) {
        params as FrameLayout.LayoutParams
        params.leftMargin = if (height > width) {
            marginLeft.toFloat().dp.toInt()
        } else {
            0
        }
    } else if (params is LinearLayout.LayoutParams) {
        params as LinearLayout.LayoutParams
        params.leftMargin = if (height > width) {
            marginLeft.toFloat().dp.toInt()
        } else {
            0
        }
    }

    layoutParams = params
}

// 设置高斯模糊
fun ImageView.setBlurImageUrl(coverUrl: String, radius: Int) {
    Glide.with(this)
        .load(coverUrl)
        .override(50)
        .dontAnimate()
        .transform(BlurTransformation())
        .into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                background = resource
            }
        })
}