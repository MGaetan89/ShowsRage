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

class ColoredToolbar @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
	private var colorFilter: PorterDuffColorFilter? = null

	@ColorInt
	private var itemColor = 0

	init {
		this.setItemColor(Utils.getContrastColor(ContextCompat.getColor(context, R.color.primary)))
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

	private fun setChildColor(child: View) {
		when (child) {
			is ImageView -> {
				child.alpha = 0.7f
				child.colorFilter = this.colorFilter
			}
			is TextView -> child.setTextColor(this.itemColor)
			is ActionMenuView -> {
				child.overflowIcon?.let {
					child.post {
						it.alpha = (0.7f * 255f).toInt()
						it.colorFilter = ColoredToolbar@ this.colorFilter
					}
				}

				(0 until child.childCount)
						.map(child::getChildAt)
						.filterIsInstance<ActionMenuItemView>()
						.forEach { actionMenuChild ->
							actionMenuChild.compoundDrawables
									.filterNotNull()
									.forEach {
										actionMenuChild.post {
											it.colorFilter = ColoredToolbar@ this.colorFilter
										}
									}
						}
			}
			is ViewGroup -> this.setGroupColor(child)
			else -> Log.d("ColoredToolbar", child.javaClass.name + " is not supported")
		}
	}

	private fun setGroupColor(group: ViewGroup?) {
		group?.let {
			for (i in 0 until it.childCount) {
				this.setChildColor(it.getChildAt(i))
			}
		}
	}
}
