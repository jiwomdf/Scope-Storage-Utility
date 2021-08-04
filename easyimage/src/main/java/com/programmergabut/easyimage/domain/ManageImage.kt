package com.programmergabut.easyimage.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.easyimage.manage.IManageImage

interface ManageImage{

    fun load(callBack: IManageImage.LoadCallBack)

    fun delete(callBack: IManageImage.DeleteCallBack)

    fun save(bitmap: Bitmap, quality: Int, callBack: IManageImage.SaveBitMapCallBack)

    fun save(base64: String, quality: Int, callBack: IManageImage.SaveBase64CallBack)

    fun save(drawable: Drawable, quality: Int, callBack: IManageImage.SaveDrawableCallBack)

}

