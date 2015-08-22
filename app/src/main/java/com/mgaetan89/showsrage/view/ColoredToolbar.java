package com.mgaetan89.showsrage.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.Utils;

public class ColoredToolbar extends Toolbar {
	private PorterDuffColorFilter colorFilter = null;

	@ColorInt
	private int itemColor = 0;

	public ColoredToolbar(Context context) {
		super(context);

		this.init(context);
	}

	public ColoredToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.init(context);
	}

	public ColoredToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		this.init(context);
	}

	@Override
	public void invalidate() {
		super.invalidate();

		this.setGroupColor(this);
	}

	public void setItemColor(@ColorInt int color) {
		this.colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
		this.itemColor = color;

		this.setGroupColor(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		this.setGroupColor(this);
	}

	private void init(Context context) {
		this.setItemColor(Utils.getContrastColor(ContextCompat.getColor(context, R.color.primary)));
	}

	private void setChildColor(View child) {
		if (child instanceof ImageView) {
			((ImageView) child).setColorFilter(this.colorFilter);
		} else if (child instanceof TextView) {
			((TextView) child).setTextColor(this.itemColor);
		} else if (child instanceof ActionMenuView) {
			ActionMenuView actionMenu = (ActionMenuView) child;

			for (int i = 0; i < actionMenu.getChildCount(); i++) {
				View actionMenuChild = actionMenu.getChildAt(i);

				if (actionMenuChild instanceof ActionMenuItemView) {
					ActionMenuItemView actionMenuItemChild = (ActionMenuItemView) actionMenuChild;

					for (int j = 0; j < actionMenuItemChild.getCompoundDrawables().length; j++) {
						final Drawable drawable = actionMenuItemChild.getCompoundDrawables()[j];

						if (drawable != null) {
							actionMenuChild.post(new Runnable() {
								@Override
								public void run() {
									drawable.setColorFilter(ColoredToolbar.this.colorFilter);
								}
							});
						}
					}
				}
			}
		} else if (child instanceof ViewGroup) {
			this.setGroupColor((ViewGroup) child);
		} else {
			Log.d("ColoredToolbar", child.getClass().getName() + " is not supported");
		}
	}

	private void setGroupColor(@Nullable ViewGroup group) {
		if (group != null) {
			for (int i = 0; i < group.getChildCount(); i++) {
				View child = group.getChildAt(i);

				this.setChildColor(child);
			}
		}
	}
}
