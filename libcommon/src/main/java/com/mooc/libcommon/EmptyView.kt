package com.mooc.libcommon

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.google.android.material.button.MaterialButton

class EmptyView : LinearLayout {
    private lateinit var icon: ImageView
    private lateinit var text: TextView
    private lateinit var action: MaterialButton

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)
        orientation = VERTICAL
        gravity = Gravity.CENTER

        icon = findViewById<ImageView>(R.id.empty_icon)
        text = findViewById<TextView>(R.id.empty_text)
        action = findViewById<MaterialButton>(R.id.empty_action)

    }

    fun setEmptyIcon(@DrawableRes iconRes: Int) {
        icon.setImageResource(iconRes)
    }

    fun setText(title: String) {
        if (TextUtils.isEmpty(title)) {
            text.visibility = GONE
        } else {
            text.text = title
            text.visibility = VISIBLE
        }
    }

    fun setButton(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            action.visibility = GONE
        } else {
            action.text = text
            action.visibility = VISIBLE
            action.setOnClickListener(listener)
        }
    }
}