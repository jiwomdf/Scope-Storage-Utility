package com.programmergabut.easyimage.domain

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.easyimage.manage.IManageImage
import java.io.IOException
import kotlin.jvm.Throws

interface ManageImage{

    /**
     * load image synchronously
     *  @return null if the file is successfully loaded, and null if the file failed to load
     */
    fun load(): Bitmap?

    /**
     * load image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun load(callBack: IManageImage.LoadCallBack)

    /**
     * delete image
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun delete(): Boolean

    /**
     * delete image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun delete(callBack: IManageImage.DeleteCallBack)

    /**
     * save bitmap formatted image
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun save(bitmap: Bitmap, quality: Int): Boolean

    /**
     * save bitmap formatted image asynchronously
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param callBack is the callback function that will called after process finish.
     */
    fun save(bitmap: Bitmap, quality: Int, callBack: IManageImage.SaveBitMapCallBack)

    /**
     * save base64 formatted image
     * @param base64 is the image in base64 string format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun save(base64: String, quality: Int): Boolean

    /**
     * save base64 formatted image asynchronously
     * @param base64 is the image in base64 string format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param callBack is the callback function that will called after process finish.
     */
    fun save(base64: String, quality: Int, callBack: IManageImage.SaveBase64CallBack)

    /**
     * save drawable formatted image
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun save(drawable: Drawable, quality: Int): Boolean

    /**
     * save drawable formatted image asynchronously
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param callBack is the callback function that will called after process finish.
     */
    fun save(drawable: Drawable, quality: Int, callBack: IManageImage.SaveDrawableCallBack)

}

