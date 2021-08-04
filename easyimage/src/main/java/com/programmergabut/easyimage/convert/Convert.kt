package com.programmergabut.easyimage.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface Convert {

    fun bitmapToBase64(bitmap: Bitmap,
                       quality: Int,
                       format: Bitmap.CompressFormat,
                       callBack: IConvertBitmap.Base64CallBack
    )

    fun base64ToBitmap(base64: String, offset: Int,
                       callBack: IConvertBitmap.BitmapCallBack
    )

    fun base64ToDrawable(base64: String, offset: Int,
                         callBack: IConvertBitmap.DrawableCallBack
    )

    fun drawableToBitmap(drawable: Drawable,
                         callBack: IConvertBitmap.BitmapCallBack
    )

    fun drawableToBase64(drawable: Drawable,
                         callBack: IConvertBitmap.Base64CallBack
    )
}