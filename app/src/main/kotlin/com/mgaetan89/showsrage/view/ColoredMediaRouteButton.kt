package com.mgaetan89.showsrage.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.MediaRouteButton
import android.util.AttributeSet

class ColoredMediaRouteButton @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MediaRouteButton(context, attrs, defStyleAttr) {
	@ColorInt
	var color = Color.WHITE

	constructor(context: Context, @ColorInt color: Int) : this(context) {
		this.color = color
	}

	override fun setRemoteIndicatorDrawable(d: Drawable?) {
		d?.let {
			// Delay the call to super so the color passed in the constructor can be assigned
			this.post {
				DrawableCompat.setTint(DrawableCompat.wrap(d), color)

				setRemoteIndicatorDrawable(d)
			}
		}
	}
}
