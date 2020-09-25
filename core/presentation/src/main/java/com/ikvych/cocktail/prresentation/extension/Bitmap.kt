package com.ikvych.cocktail.prresentation.extension

import android.graphics.Bitmap
import android.os.Build
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

fun convertMbToBinaryBytes(mb: Long): Long {
    return (mb * 2.0F.pow(20)).toLong()
}

val Bitmap.Config.bytePerPixel: Int
    get() {
        return if (Build.VERSION.SDK_INT >= 26) {
            when (this) {
                Bitmap.Config.ALPHA_8 -> 1
                Bitmap.Config.RGB_565 -> 2
                Bitmap.Config.ARGB_4444 -> 2
                Bitmap.Config.ARGB_8888 -> 4
                Bitmap.Config.RGBA_F16 -> 8
                Bitmap.Config.HARDWARE -> 8
            }
        } else {
            when {
                this == Bitmap.Config.ALPHA_8 -> 1
                this == Bitmap.Config.ALPHA_8 -> 1
                this == Bitmap.Config.RGB_565 || this == Bitmap.Config.ARGB_4444 -> 2
                this == Bitmap.Config.ARGB_8888 -> 4
                else -> 8
            }
        }
    }

fun Bitmap.scaleToSize(maxBytes: Long): Bitmap {
    val currentPixels = width * height
    val maxPixels = maxBytes / config.bytePerPixel
    return if (currentPixels <= maxPixels) this
    else {
        // Scaling factor when maintaining aspect ratio is the square root since x and y have a relation
        val downScaleFactor = sqrt(maxPixels / currentPixels.toDouble())
        val newWidth = floor(width * downScaleFactor).toInt()
        val newHeight = floor(height * downScaleFactor).toInt()
        Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
    }
}