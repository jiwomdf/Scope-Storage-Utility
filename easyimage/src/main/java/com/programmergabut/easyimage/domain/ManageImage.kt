package com.programmergabut.easyimage.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.easyimage.manage.IManageImage

interface ManageImage{

    /**
     * load image
     * @param callBack is the callback function that will called after process finish
     */
    fun load(callBack: IManageImage.LoadCallBack)

    /**
     * delete image
     * @param callBack is the callback function that will called after process finish
     */
    fun delete(callBack: IManageImage.DeleteCallBack)

    /**
     * save bitmap formatted image
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100
     * @param callBack is the callback function that will called after process finish
     */
    fun save(bitmap: Bitmap, quality: Int, callBack: IManageImage.SaveBitMapCallBack)

    /**
     * save base64 formatted image
     * @param base64 is the image in base64 string format.
     * @param quality is the quality of the image, must be between 0 and 100
     * @param callBack is the callback function that will called after process finish
     */
    fun save(base64: String, quality: Int, callBack: IManageImage.SaveBase64CallBack)

    /**
     * save drawable formatted image
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100
     * @param callBack is the callback function that will called after process finish
     */
    fun save(drawable: Drawable, quality: Int, callBack: IManageImage.SaveDrawableCallBack)

}

