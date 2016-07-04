package com.mgaetan89.showsrage.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.ActionMenuView
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.Utils

class ColoredToolbar : Toolbar {
    private var colorFilter: PorterDuffColorFilter? = null

    @ColorInt
    private var itemColor: Int = 0

    constructor(context: Context) : super(context) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(context)
    }

    override fun invalidate() {
        super.invalidate()

        this.setGroupColor(this)
    }

    fun setItemColor(@ColorInt color: Int) {
        this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        this.itemColor = color

        this.setGroupColor(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        this.setGroupColor(this)
    }

    private fun init(context: Context) {
        this.setItemColor(Utils.getContrastColor(ContextCompat.getColor(context, R.color.primary)))
    }

    private fun setChildColor(child: View) {
        if (child is ImageView) {
            child.alpha = 0.7f
            child.colorFilter = this.colorFilter
        } else if (child is TextView) {
            child.setTextColor(this.itemColor)
        } else if (child is ActionMenuView) {
            val overflowIcon = child.overflowIcon

            if (overflowIcon != null) {
                child.post {
                    overflowIcon.alpha = (0.7f * 255f).toInt()
                    overflowIcon.colorFilter = ColoredToolbar@this.colorFilter
                }
            }

            for (i in 0..(child.childCount - 1)) {
                val actionMenuChild = child.getChildAt(i)

                if (actionMenuChild is ActionMenuItemView) {
                    for (j in 0..(actionMenuChild.compoundDrawables.size - 1)) {
                        val drawable = actionMenuChild.compoundDrawables[j]

                        if (drawable != null) {
                            actionMenuChild.post {
                                drawable.colorFilter = ColoredToolbar@this.colorFilter
                            }
                        }
                    }
                }
            }
        } else if (child is ViewGroup) {
            this.setGroupColor(child)
        } else {
            Log.d("ColoredToolbar", child.javaClass.name + " is not supported")
        }
    }

    private fun setGroupColor(group: ViewGroup?) {
        if (group != null) {
            for (i in 0..(group.childCount - 1)) {
                this.setChildColor(group.getChildAt(i))
            }
        }
    }
}
