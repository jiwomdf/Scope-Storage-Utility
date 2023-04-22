package com.programmergabut.scopestorageutility.manage.callback

import com.programmergabut.scopestorageutility.base.callback.BaseCallback
import java.io.OutputStream

interface OutputStreamCallback: BaseCallback {
    fun onSuccess(outputStream: OutputStream)
}