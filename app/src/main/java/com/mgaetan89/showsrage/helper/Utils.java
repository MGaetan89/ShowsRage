package com.mgaetan89.showsrage.helper;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public abstract class Utils {
	public static int getContrastColor(@ColorInt int color) {
		double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;

		return y >= 128 ? Color.BLACK : Color.WHITE;
	}
}
