package com.mgaetan89.showsrage.helper

import android.graphics.*
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource

// From: glide-transformations
// Version: 1.0.5
// Link: https://github.com/wasabeef/glide-transformations/blob/1.0.5/transformations/src/main/java/jp/wasabeef/glide/transformations/CropCircleTransformation.java
class GlideCircleTransformation(private val bitmapPool: BitmapPool) : Transformation<Bitmap> {
    override fun transform(resource: Resource<Bitmap>?, outWidth: Int, outHeight: Int): Resource<Bitmap>? {
        val source = resource?.get()
        val size = Math.min(source?.width ?: 0, source?.height ?: 0)
        val height = ((source?.height ?: 0) - size) / 2
        val width = ((source?.width ?: 0) - size) / 2

        var bitmap = this.bitmapPool.get(size, size, Bitmap.Config.ARGB_8888)

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        if (height != 0 || width != 0) {
            val matrix = Matrix()
            matrix.setTranslate(-width.toFloat(), -height.toFloat())

            shader.setLocalMatrix(matrix)
        }

        paint.setShader(shader)
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        return BitmapResource.obtain(bitmap, this.bitmapPool)
    }

    override fun getId(): String? {
        return "CropCircleTransformation()"
    }
}
