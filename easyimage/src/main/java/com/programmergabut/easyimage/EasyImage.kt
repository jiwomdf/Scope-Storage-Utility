package com.programmergabut.easyimage

import android.content.Context
import com.programmergabut.easyimage.convert.Convert
import com.programmergabut.easyimage.convert.ConvertImpl
import com.programmergabut.easyimage.domain.Manage
import com.programmergabut.easyimage.manage.ManageImpl

class EasyImage {

    companion object {
        val convert = ConvertImpl() as Convert
        fun manage(context: Context) = ManageImpl(context) as Manage
    }

}