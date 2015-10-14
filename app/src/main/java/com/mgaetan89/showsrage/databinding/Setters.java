package com.mgaetan89.showsrage.databinding;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

public class Setters {
	@BindingAdapter("bind:textColorRes")
	public static void setTextColorRes(TextView textView, @ColorRes int colorRes) {
		textView.setTextColor(ContextCompat.getColor(textView.getContext(), colorRes));
	}

	@BindingAdapter("bind:textRes")
	public static void setTextRes(TextView textView, @StringRes int textRes) {
		if (textRes == 0) {
			textView.setText("");
		} else {
			textView.setText(textRes);
		}
	}
}
