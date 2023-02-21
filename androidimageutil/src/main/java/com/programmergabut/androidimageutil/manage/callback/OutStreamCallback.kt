package com.programmergabut.androidimageutil.manage.callback

import com.programmergabut.androidimageutil.base.callback.BaseCallback
import java.io.OutputStream

interface OutStreamCallback: BaseCallback {
    fun onSuccess(outputStream: OutputStream)
}