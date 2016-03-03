package com.mgaetan89.showsrage.databinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.helper.ImageLoader;

public class Setters {
	@BindingAdapter("android:backgroundTint")
	public static void setBackgroundTint(TextView textView, @ColorRes int tint) {
		if (tint != 0) {
			Drawable background = DrawableCompat.wrap(textView.getBackground());
			DrawableCompat.setTint(background, ContextCompat.getColor(textView.getContext(), tint));
		}
	}

	@BindingAdapter({"bind:imageUrl", "bind:circle"})
	public static void setImageUrl(ImageView imageView, String imageUrl, boolean circle) {
		ImageLoader.INSTANCE.load(imageView, imageUrl, circle);
	}

	@BindingAdapter("bind:selected")
	public static void setSelected(TextView textView, boolean selected) {
		textView.setSelected(selected);
	}

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
