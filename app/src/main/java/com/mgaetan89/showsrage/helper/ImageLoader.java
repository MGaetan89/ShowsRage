package com.mgaetan89.showsrage.helper;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public abstract class ImageLoader {
	public static void load(@Nullable ImageView imageView, String url, boolean circleTransform) {
		load(imageView, url, circleTransform, null);
	}

	public static void load(@Nullable ImageView imageView, String url, boolean circleTransform, @Nullable final Palette.PaletteAsyncListener paletteListener) {
		if (imageView == null) {
			return;
		}

		BitmapRequestBuilder<String, Bitmap> glide = Glide.with(imageView.getContext())
				.load(url)
				.asBitmap()
				.diskCacheStrategy(DiskCacheStrategy.ALL);

		if (circleTransform) {
			BitmapPool bitmapPool = Glide.get(imageView.getContext()).getBitmapPool();

			glide.transform(new GlideCircleTransformation(bitmapPool));
		}

		glide.into(new BitmapImageViewTarget(imageView) {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				super.onResourceReady(resource, glideAnimation);

				if (paletteListener != null) {
					new Palette.Builder(resource).generate(paletteListener);
				}
			}
		});
	}
}
