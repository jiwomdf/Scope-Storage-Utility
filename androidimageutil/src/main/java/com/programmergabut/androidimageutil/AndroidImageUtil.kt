package com.programmergabut.androidimageutil

import android.content.Context
import com.programmergabut.androidimageutil.manage.Manage

class AndroidImageUtil {
    companion object {
        const val TAG = "AndroidImageUtil"
        fun manage(context: Context) = Manage(context)
    }
}