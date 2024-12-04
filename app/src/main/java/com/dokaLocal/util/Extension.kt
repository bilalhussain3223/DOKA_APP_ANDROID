package com.dokaLocal.util

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.WindowManager
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream


fun Activity.setAppSettings() {
    val lp = window.attributes
    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    window.attributes = lp
}

fun ResponseBody.getBitmap(): Bitmap {
    return BitmapFactory.decodeStream(this.byteStream())
}

fun Bitmap.adjustedImage(): Bitmap {
//    return flippedHorizontally().flippedVertically().noir()
    return flippedVertically().noir()
}

fun Bitmap.flippedHorizontally(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.flippedVertically(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.noir(): Bitmap {
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(cm)
    val bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888)
    val c = Canvas(bmpGrayscale)
    val paintDraw = Paint()
    c.drawBitmap(this, 0f, 0f, paintDraw)
    c.drawBitmap(bmpGrayscale, 0f, 0f, paint)
    return bmpGrayscale
}

fun Bitmap.negative(): Bitmap {
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x
            var pixel = pixels[index]

            val alpha = Color.alpha(pixel)
            var red = 255 - Color.red(pixel)
            var green = 255 - Color.green(pixel)
            var blue = 255 - Color.blue(pixel)

            pixel = Color.argb(alpha, red, green, blue)
            pixels[index] = pixel
        }
    }

    return Bitmap.createBitmap(pixels, width, height, Config.ARGB_8888)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(
        this,
        0,
        0,
        width,
        height,
        matrix,
        true
    )
}


private fun scaleBitmapTo(
    destinationHeight: Int,
    destinationWidth: Int,
    bitmap: Bitmap?
): Bitmap {
    val scaledBitmap =
        Bitmap.createScaledBitmap(bitmap!!, destinationWidth, destinationHeight, false)
    val outStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outStream)
    return scaledBitmap
}

fun loadCompressedBitmap(bitmap: Bitmap): Bitmap {
    val scaledBitmap: Bitmap
    val origWidth = bitmap.width
    val origHeight = bitmap.height
    val destHeight: Int
    val destWidth: Int
    if (origWidth > 3000) {
        destWidth = origWidth / 5
        destHeight = origHeight / 5
        scaledBitmap = scaleBitmapTo(destHeight, destWidth, bitmap)
    } else if (origWidth > 1000) {
        destHeight = origHeight / 2
        destWidth = origWidth / 2
        scaledBitmap = scaleBitmapTo(destHeight, destWidth, bitmap)
    } else scaledBitmap = bitmap
    return scaledBitmap
}

fun changeTint(bitmap: Bitmap, tintValue: Float): Bitmap {
    val tintedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(tintedBitmap)
    val paint = Paint()
    val tintColor = when {
        tintValue < 0 -> {
            val blue = 255
            val greenComponent = (255 * (1 - tintValue)).toInt()
            Color.argb(40, 0, greenComponent, blue)
        }

        tintValue > 0 -> {
            val redComponent = 255
            val greenComponent = (255 * (1 - tintValue)).toInt()
            Color.argb(40, redComponent, greenComponent, 0)
        }

        else -> {
            Color.argb(0, 0, 0, 0)
        }
    }
    paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return tintedBitmap
}


fun changeBitmapSaturationOptimized(bitmap: Bitmap, saturation: Float): Bitmap {
    val scale = 0.5f
    val scaledBitmap = Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width * scale).toInt(),
        (bitmap.height * scale).toInt(),
        true
    )
    val newBitmap =
        Bitmap.createBitmap(scaledBitmap.width, scaledBitmap.height, scaledBitmap.config)
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(saturation)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    val canvas = Canvas(newBitmap)
    canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)
    return Bitmap.createScaledBitmap(newBitmap, bitmap.width, bitmap.height, true)
}

fun changeBitmapSaturationOld(bitmap: Bitmap, saturation: Float): Bitmap {
    val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(newBitmap)
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(saturation)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return newBitmap
}

fun changeExposure(bitmap: Bitmap, exposure: Float): Bitmap {
    val adjustedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val canvas = Canvas(adjustedBitmap)
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
            setScale(exposure, exposure, exposure, 1f)
        })
    }
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return adjustedBitmap
}

fun changeContrast(bitmap: Bitmap, contrast: Float): Bitmap {
    val adjustedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
    val cm = ColorMatrix()
    cm.set(
        floatArrayOf(
            contrast, 0f, 0f, 0f, 0f,
            0f, contrast, 0f, 0f, 0f,
            0f, 0f, contrast, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    val cf = ColorMatrixColorFilter(cm)
    val paint = Paint()
    paint.colorFilter = cf
    val canvas = Canvas(adjustedBitmap)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return adjustedBitmap
}

@SuppressLint("DefaultLocale")
fun textTintFormat(value: Float): String {
    var formatted = if (value == value.toLong().toFloat()) {
        String.format("%.0f", value)
    } else {
        String.format("%.2f", value)
    }
    if (formatted == "-0.00" || formatted == "0.00") {
        formatted = "0"
    }
    return formatted
}



