package com.programmergabut.androidimageutil

import android.content.Context
import com.programmergabut.androidimageutil.convert.Convert
import com.programmergabut.androidimageutil.convert.ConvertImpl
import com.programmergabut.androidimageutil.domain.Manage
import com.programmergabut.androidimageutil.manage.ManageImpl

class AndroidImageUtil {

    companion object {
        const val TAG = "AndroidImageUtil"
        val convert = ConvertImpl() as Convert
        fun manage(context: Context) = ManageImpl(context) as Manage
    }

}