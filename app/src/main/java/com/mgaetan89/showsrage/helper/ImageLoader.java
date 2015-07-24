package com.mgaetan89.showsrage.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
	public interface OnImageResult {
		void onImageError(ImageView imageView, @Nullable Exception exception, @Nullable Drawable errorDrawable);

		void onImageReady(ImageView imageView, Bitmap resource);
	}

	public static void load(@Nullable ImageView imageView, String url, boolean circleTransform) {
		load(imageView, url, circleTransform, null, null);
	}

	public static void load(@Nullable ImageView imageView, String url, boolean circleTransform, @Nullable final Palette.PaletteAsyncListener paletteListener, @Nullable final OnImageResult onImageResult) {
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

		glide.into(new BitmapTarget(imageView, paletteListener, onImageResult));
	}

	private static final class BitmapTarget extends BitmapImageViewTarget {
		private OnImageResult onImageResult;
		private Palette.PaletteAsyncListener paletteListener;

		private BitmapTarget(ImageView view, Palette.PaletteAsyncListener paletteListener, OnImageResult onImageResult) {
			super(view);

			this.onImageResult = onImageResult;
			this.paletteListener = paletteListener;
		}

		@Override
		public void onLoadFailed(Exception e, Drawable errorDrawable) {
			super.onLoadFailed(e, errorDrawable);

			if (this.onImageResult != null) {
				this.onImageResult.onImageError(this.getView(), e, errorDrawable);
			}
		}

		@Override
		public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
			super.onResourceReady(resource, glideAnimation);

			if (this.onImageResult != null) {
				this.onImageResult.onImageReady(this.getView(), resource);
			}

			if (this.paletteListener != null) {
				new Palette.Builder(resource).generate(this.paletteListener);
			}
		}
	}
}
