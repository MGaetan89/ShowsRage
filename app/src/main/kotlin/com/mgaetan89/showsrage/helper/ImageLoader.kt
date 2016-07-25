package com.mgaetan89.showsrage.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.IdRes
import android.support.v7.graphics.Palette
import android.widget.ImageView
import android.widget.RemoteViews
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.CropCircleTransformation

object ImageLoader {
    interface OnImageResult {
        fun onImageError(imageView: ImageView, exception: Exception?, errorDrawable: Drawable?)

        fun onImageReady(imageView: ImageView, resource: Bitmap?)
    }

    fun load(imageView: ImageView?, url: String?, circleTransform: Boolean) {
        this.load(imageView, url, circleTransform, null, null)
    }

    fun load(imageView: ImageView?, url: String?, circleTransform: Boolean, paletteListener: Palette.PaletteAsyncListener?, onImageResult: OnImageResult?) {
        val context = imageView?.context ?: return

        this.getGlideInstance(context, url, circleTransform)?.let {
            it.into(BitmapTarget(imageView!!, paletteListener, onImageResult))
        }
    }

    fun load(context: Context, remoteViews: RemoteViews, @IdRes viewId: Int, url: String?, circleTransform: Boolean) {
        this.getGlideInstance(context, url, circleTransform)?.let {
            val bitmap = it.into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

            remoteViews.setImageViewBitmap(viewId, bitmap.get())

            Glide.clear(bitmap)
        }
    }

    private fun getGlideInstance(context: Context, url: String?, circleTransform: Boolean): BitmapRequestBuilder<String, Bitmap>? {
        val glide = Glide.with(context)
                .load(url)
                .asBitmap()
                .approximate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

        if (circleTransform) {
            glide.transform(CropCircleTransformation(context))
        }

        return glide
    }

    private class BitmapTarget(view: ImageView, val paletteListener: Palette.PaletteAsyncListener?, val onImageResult: OnImageResult?) : BitmapImageViewTarget(view) {
        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
            super.onLoadFailed(e, errorDrawable)

            this.onImageResult?.onImageError(this.view, e, errorDrawable)
        }

        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
            super.onResourceReady(resource, glideAnimation)

            this.onImageResult?.onImageReady(this.view, resource)

            if (this.paletteListener != null) {
                Palette.Builder(resource).generate(this.paletteListener)
            }
        }
    }
}
