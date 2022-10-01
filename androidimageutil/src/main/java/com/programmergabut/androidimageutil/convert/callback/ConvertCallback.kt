package com.programmergabut.androidimageutil.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.androidimageutil.base.callback.BaseCallback

interface Base64Callback : BaseCallback {
    fun onResult(base64: String)
}

interface DrawableCallback : BaseCallback {
    fun onResult(drawable: Drawable)
}

interface BitmapCallback : BaseCallback {
    fun onResult(bitmap: Bitmap)
}
