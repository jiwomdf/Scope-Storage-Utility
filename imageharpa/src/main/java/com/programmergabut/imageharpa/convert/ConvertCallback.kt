package com.programmergabut.imageharpa.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.imageharpa.base.BaseCallback

interface Base64Callback : BaseCallback {
    fun onResult(base64: String)
}
interface DrawableCallback : BaseCallback {
    fun onResult(drawable: Drawable)
}

interface BitmapCallback : BaseCallback {
    fun onResult(bitmap: Bitmap)
}
