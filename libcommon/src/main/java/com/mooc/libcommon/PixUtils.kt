package com.mooc.libcommon

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

class PixUtils {
    companion object {
        // 获取屏幕宽度
        @JvmStatic
        fun getScreenWidth(): Int {
            return AppGlobals.getApplication().resources.displayMetrics.widthPixels
        }

        // 获取屏幕高度
        @JvmStatic
        fun getScreenHeight(): Int {
            return AppGlobals.getApplication().resources.displayMetrics.heightPixels
        }
    }
}

// 将dp转为px
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )