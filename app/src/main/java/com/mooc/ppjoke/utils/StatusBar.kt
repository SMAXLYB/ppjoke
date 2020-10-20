package com.mooc.ppjoke.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

object StatusBar {
    /**
     * 6.0级以上的沉浸式布局
     *
     * @param activity
     */
    fun fitSystemBar(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val window = activity.window
        val decorView = window.decorView
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN--能够使得页面布局延伸到状态栏之下，但不会隐藏状态栏。
        //View.SYSTEM_UI_FLAG_FULLSCREEN -- 能够使得页面布局延伸到状态栏，但是会隐藏状态栏。
        //相当于WindowManager.LayoutParams.FLAG_FULLSCREEN
        // View.SYSTEM_UI_FLAG_LAYOUT_STABLE 3大金刚键是否隐藏对布局不影响
        // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 白底黑字
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        // 允许绘制颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 6.0及以上的状态栏色调
     *
     * @param activity
     * @param light    true:白底黑字,false:黑底白字
     */
    fun lightStatusBar(activity: Activity, light: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val window = activity.window
        val decorView = window.decorView
        var visibility = decorView.systemUiVisibility
        visibility = if (light) {
            visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = visibility
    }
}
