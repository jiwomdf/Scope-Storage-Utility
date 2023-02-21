package com.programmergabut.androidimageutil.manage.callback

import android.graphics.Bitmap
import com.programmergabut.androidimageutil.base.callback.BaseCallback

interface ImageCallback: BaseCallback {
    fun onSuccess()
}

interface LoadImageCallback: BaseCallback {
    fun onResult(bitmap: Bitmap?)
}
