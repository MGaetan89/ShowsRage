package com.mgaetan89.showsrage.databinding

import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView
import android.widget.TextView
import com.mgaetan89.showsrage.helper.ImageLoader

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(textView: TextView, @ColorRes tint: Int) {
    if (tint != 0) {
        val background = DrawableCompat.wrap(textView.background)
        DrawableCompat.setTint(background, ContextCompat.getColor(textView.context, tint))
    }
}

@BindingAdapter("imageUrl", "circle")
fun setImageUrl(imageView: ImageView, imageUrl: String?, circle: Boolean) {
    ImageLoader.load(imageView, imageUrl, circle)
}

@BindingAdapter("selected")
fun setSelected(textView: TextView, selected: Boolean) {
    textView.isSelected = selected
}

@BindingAdapter("textColorRes")
fun setTextColorRes(textView: TextView, @ColorRes colorRes: Int) {
    textView.setTextColor(ContextCompat.getColor(textView.context, colorRes))
}

@BindingAdapter("textRes")
fun setTextRes(textView: TextView, @StringRes textRes: Int) {
    if (textRes == 0) {
        textView.text = ""
    } else {
        textView.setText(textRes)
    }
}
