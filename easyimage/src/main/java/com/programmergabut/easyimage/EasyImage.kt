package com.programmergabut.easyimage

import android.content.Context
import com.programmergabut.easyimage.convert.ConvertBitmap
import com.programmergabut.easyimage.convert.ConvertDrawable
import com.programmergabut.easyimage.domain.ManageImage
import com.programmergabut.easyimage.manage.ManageImageImpl

class EasyImage {

    class Convert {
        companion object {
            val bitmap = ConvertBitmap()
            val drawable = ConvertDrawable()
        }
    }

    class Manage(private val context: Context){
        fun setFileAttribute(
            fileName: String,
            directory: String,
            fileExtension: Extension
        ) = ManageImageImpl(
            context,
            fileName,
            directory,
            fileExtension
        ) as ManageImage
    }


}