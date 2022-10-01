package com.programmergabut.androidimageutil

import android.content.Context
import com.programmergabut.androidimageutil.convert.Convert
import com.programmergabut.androidimageutil.manage.Manage

class AndroidImageUtil {
    companion object {
        const val TAG = "AndroidImageUtil"
        val convert = Convert()
        fun manage(context: Context) = Manage(context)
    }
}