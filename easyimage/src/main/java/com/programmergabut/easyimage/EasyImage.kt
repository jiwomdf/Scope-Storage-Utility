package com.programmergabut.easyimage

import android.content.Context
import com.programmergabut.easyimage.convert.ConvertBitmap
import com.programmergabut.easyimage.convert.ConvertDrawable
import com.programmergabut.easyimage.manage.LoadFile

class EasyImage {

    class Converter {
        companion object {
            val bitmap = ConvertBitmap()
            val drawable = ConvertDrawable()
        }
    }

    class Manage(private val context: Context) {
        fun loadFile() = LoadFile(context)
    }


}