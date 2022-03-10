package com.programmergabut.imageharpa.manage

import android.graphics.Bitmap
import com.programmergabut.imageharpa.base.BaseCallback

interface ImageCallback: BaseCallback {
    fun onSuccess()
}

interface LoadImageCallback: BaseCallback {
    fun onResult(bitmap: Bitmap?)
}
