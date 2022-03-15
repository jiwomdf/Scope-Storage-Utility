package com.programmergabut.imageharpa

import android.content.Context
import com.programmergabut.imageharpa.convert.Convert
import com.programmergabut.imageharpa.convert.ConvertImpl
import com.programmergabut.imageharpa.domain.Manage
import com.programmergabut.imageharpa.manage.ManageImpl

class ImageHarpa {

    companion object {
        const val TAG = "ImageHarpa"
        val convert = ConvertImpl() as Convert
        fun manage(context: Context) = ManageImpl(context) as Manage
    }

}