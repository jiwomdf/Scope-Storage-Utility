package com.programmergabut.easyimage.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.easyimage.base.BaseCallBack

interface IConvertBitmap {

    interface Base64CallBack : BaseCallBack {
        fun onResult(base64: String)
    }
    interface DrawableCallBack : BaseCallBack {
        fun onResult(drawable: Drawable)
    }

    interface BitmapCallBack : BaseCallBack {
        fun onResult(bitmap: Bitmap)
    }


}