package com.programmergabut.scopestorageutility.manage.callback

import com.programmergabut.scopestorageutility.base.callback.BaseCallback
import java.io.OutputStream

interface OutStreamCallback: BaseCallback {
    fun onSuccess(outputStream: OutputStream)
}