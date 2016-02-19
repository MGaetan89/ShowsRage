package com.mgaetan89.showsrage.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.app.MediaRouteActionProvider
import android.support.v7.app.MediaRouteButton

class ColoredMediaRouteActionProvider(context: Context) : MediaRouteActionProvider(context) {
    @ColorInt
    var buttonColor = Color.WHITE

    override fun onCreateMediaRouteButton(): MediaRouteButton? {
        return ColoredMediaRouteButton(this.context, this.buttonColor)
    }
}
