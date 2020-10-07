package com.mooc.ppjoke.view

import android.content.res.Resources
import android.util.TypedValue
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.mooc.ppjoke.R
import com.mooc.ppjoke.utils.AppConfig

class AppBottomBar : BottomNavigationView {
    // 图标
    companion object {
        private val sIcons = intArrayOf(
            R.drawable.icon_tab_home,
            R.drawable.icon_tab_sofa,
            R.drawable.icon_tab_publish,
            R.drawable.icon_tab_find,
            R.drawable.icon_tab_mine
        )
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        // 获取所有tab信息
        val bottomBar = AppConfig.getBottomBar()
        val tabs = bottomBar.tabs

        // 设置颜色
        // 设置选中状态
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()

        // 设置选中状态对相应的颜色
        val colors = intArrayOf(
            Color.parseColor(bottomBar.activeColor),
            Color.parseColor(bottomBar.inActiveColor)
        )
        val colorStateList = ColorStateList(states, colors)

        // 图标颜色
        itemIconTintList = colorStateList
        // 文字颜色
        itemTextColor = colorStateList
        // 设置选中时文字是否显示
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

        // 将bar添加到bottomView中
       tabs.forEach {

           if (!it.enable) {
               return@forEach
           }
           val id = getId(it.pageUrl)
           if (id < 0) {
               return@forEach
           }
           // 设置文字
           val item = menu.add(0, id, it.index, it.title)

           // 设置图标
           item.setIcon(sIcons[it.index])
       }

       // 为了保证顺序,在添加图标的时候会清除之前的所有图标,然后重新排序添加
       // 所以在这里等所有图标添加完毕之后再设置大小
       tabs.forEach {

           if (!it.enable) {
               return@forEach
           }
           val id = getId(it.pageUrl)
           if (id < 0) {
               return@forEach
           }

           val iconSize = it.size.toFloat().dp
           // 获取到itemView
           val menuView = getChildAt(0) as BottomNavigationMenuView
           val itemView = menuView.getChildAt(it.index) as BottomNavigationItemView
           itemView.setIconSize(iconSize.toInt())

           // 如果是中间tab,进行着色
           if (TextUtils.isEmpty(it.title)) {
               itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(it.tintColor)))
               // 无需浮动效果
               itemView.setShifting(false)
           }
       }

        // 设置默认选中
        if (bottomBar.selectTab != 0) {
            val tab = tabs[bottomBar.selectTab]
            if (tab.enable) {
                val id = getId(tab.pageUrl)
                post { selectedItemId = id }
            }
        }
    }

    private fun getId(pageUrl: String): Int {
        val destination = AppConfig.getDestConfig()[pageUrl]

        return destination?.id ?: -1
    }
}

// 获取以dp转为px的值
val Float.dp
   get() = TypedValue.applyDimension(
       TypedValue.COMPLEX_UNIT_DIP,
       this,
       Resources.getSystem().displayMetrics
   )