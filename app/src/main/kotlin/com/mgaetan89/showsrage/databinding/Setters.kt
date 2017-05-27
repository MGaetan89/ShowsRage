package com.mgaetan89.showsrage.databinding

import android.databinding.BindingAdapter
import android.support.annotation.StringRes
import android.widget.ImageView
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.ImageLoader

@BindingAdapter("imageUrl", "circle")
fun setImageUrl(imageView: ImageView, imageUrl: String?, circle: Boolean) {
	val currentUrl = imageView.getTag(R.id.image_view_url)

	if (currentUrl == null || currentUrl != imageUrl) {
		ImageLoader.load(imageView, imageUrl, circle)

		imageView.setTag(R.id.image_view_url, imageUrl)
	}
}

@BindingAdapter("selected")
fun setSelected(textView: TextView, selected: Boolean) {
	textView.isSelected = selected
}

@BindingAdapter("textRes")
fun setTextRes(textView: TextView, @StringRes textRes: Int) {
	if (textRes == 0) {
		textView.text = ""
	} else {
		textView.setText(textRes)
	}
}
