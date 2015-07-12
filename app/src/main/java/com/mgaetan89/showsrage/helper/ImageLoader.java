package com.mgaetan89.showsrage.helper;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public abstract class ImageLoader {
	public static void load(@Nullable ImageView imageView, String url, boolean circleTransform) {
		if (imageView == null) {
			return;
		}

		DrawableRequestBuilder<String> glide = Glide.with(imageView.getContext())
				.load(url)
				.diskCacheStrategy(DiskCacheStrategy.ALL);

		if (circleTransform) {
			BitmapPool bitmapPool = Glide.get(imageView.getContext()).getBitmapPool();

			glide.bitmapTransform(new GlideCircleTransformation(bitmapPool));
		}

		glide.into(imageView);
	}
}
