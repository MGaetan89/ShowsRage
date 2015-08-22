package com.mgaetan89.showsrage.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.MediaRouteButton;
import android.util.AttributeSet;

public class ColoredMediaRouteButton extends MediaRouteButton {
	@ColorInt
	private int color = Color.WHITE;

	public ColoredMediaRouteButton(Context context) {
		super(context);
	}

	public ColoredMediaRouteButton(Context context, @ColorInt int color) {
		super(context);

		this.color = color;
	}

	public ColoredMediaRouteButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColoredMediaRouteButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setRemoteIndicatorDrawable(final Drawable d) {
		// Delay the call to super so the color passed in the constructor can be assigned
		this.post(new Runnable() {
			@Override
			public void run() {
				DrawableCompat.setTint(DrawableCompat.wrap(d), ColoredMediaRouteButton.this.color);

				ColoredMediaRouteButton.super.setRemoteIndicatorDrawable(d);
			}
		});
	}
}
