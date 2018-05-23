package com.mgaetan89.showsrage.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.IdRes
import android.support.annotation.WorkerThread
import android.support.v7.graphics.Palette
import android.widget.ImageView
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

object ImageLoader {
	interface OnImageResult {
		fun onImageError(imageView: ImageView, errorDrawable: Drawable?)

		fun onImageReady(imageView: ImageView, resource: Bitmap)
	}

	fun getBitmap(context: Context, url: String?, circleTransform: Boolean)
			= this.getGlideInstance(context, url, circleTransform).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

	fun load(imageView: ImageView?, url: String?, circleTransform: Boolean) {
		this.load(imageView, url, circleTransform, null, null)
	}

	fun load(imageView: ImageView?, url: String?, circleTransform: Boolean, paletteListener: Palette.PaletteAsyncListener?, onImageResult: OnImageResult?) {
		val context = imageView?.context ?: return

		this.getGlideInstance(context, url, circleTransform).into(BitmapTarget(imageView, paletteListener, onImageResult))
	}

	@WorkerThread
	fun load(context: Context, remoteViews: RemoteViews, @IdRes viewId: Int, url: String?, circleTransform: Boolean) {
		this.getBitmap(context, url, circleTransform).let {
			remoteViews.setImageViewBitmap(viewId, it.get())

			Glide.with(context.applicationContext).clear(it)
		}
	}

	private fun getGlideInstance(context: Context, url: String?, circleTransform: Boolean): RequestBuilder<Bitmap> {
		val requestOptions = RequestOptions()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.apply {
				if (circleTransform) {
					this.circleCrop()
				}
			}

		return Glide.with(context.applicationContext)
			.asBitmap()
			.load(url)
			.apply(requestOptions)
	}

	private class BitmapTarget(view: ImageView, val paletteListener: Palette.PaletteAsyncListener?, val onImageResult: OnImageResult?) : BitmapImageViewTarget(view) {
		override fun onLoadFailed(errorDrawable: Drawable?) {
			super.onLoadFailed(errorDrawable)

			this.onImageResult?.onImageError(this.view, errorDrawable)
		}

		override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            this.view.setImageBitmap(resource)

			this.onImageResult?.onImageReady(this.view, resource)

			this.paletteListener?.let {
				Palette.Builder(resource).generate(it)
			}
		}
	}
}
