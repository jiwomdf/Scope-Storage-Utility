package com.programmergabut.easyimage

import android.content.Context
import com.programmergabut.easyimage.convert.ConvertBitmap
import com.programmergabut.easyimage.convert.ConvertDrawable
import com.programmergabut.easyimage.manage.LoadImage
import com.programmergabut.easyimage.manage.SaveImage

class EasyImage {

    class Convert {
        companion object {
            val bitmap = ConvertBitmap()
            val drawable = ConvertDrawable()
        }
    }

    class Manage(private val context: Context) {
        fun setFileAttribute(
            fileName: String,
            directory: String,
            fileExtension: Extension
        ) = LoadImage(
            context,
            fileName,
            directory,
            fileExtension
        )
    }


}