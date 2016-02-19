package com.mgaetan89.showsrage.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.MediaRouteButton
import android.util.AttributeSet

class ColoredMediaRouteButton : MediaRouteButton {
    @ColorInt
    var color = Color.WHITE

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, @ColorInt color: Int) : super(context) {
        this.color = color
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun setRemoteIndicatorDrawable(d: Drawable?) {
        // Delay the call to super so the color passed in the constructor can be assigned
        this.post {
            DrawableCompat.setTint(DrawableCompat.wrap(d), ColoredMediaRouteButton@this.color)

            ColoredMediaRouteButton@super.setRemoteIndicatorDrawable(d)
        }
    }
}
