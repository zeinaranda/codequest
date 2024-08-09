package com.dicoding.testcodequest.data

import android.graphics.*
import android.graphics.Bitmap
import android.service.autofill.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class GrayscaleTransformation : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }

        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray())
    }

    companion object {
        private const val ID = "com.example.GrayscaleTransformation"
    }
}