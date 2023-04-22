package com.programmergabut.scopestorageutility.manage.callback

import android.graphics.Bitmap
import com.programmergabut.scopestorageutility.base.callback.BaseCallback

interface ImageCallback: BaseCallback {
    fun onSuccess()
}

interface LoadImageCallback: BaseCallback {
    fun onResult(bitmap: Bitmap?)
}
