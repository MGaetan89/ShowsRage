package com.mgaetan89.showsrage.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;

public class ColoredMediaRouteActionProvider extends MediaRouteActionProvider {
	@ColorInt
	private int buttonColor = Color.WHITE;

	public ColoredMediaRouteActionProvider(Context context) {
		super(context);
	}

	@Override
	public MediaRouteButton onCreateMediaRouteButton() {
		return new ColoredMediaRouteButton(this.getContext(), this.buttonColor);
	}

	public void setButtonColor(int buttonColor) {
		this.buttonColor = buttonColor;
	}
}
