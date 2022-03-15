package com.programmergabut.imageharpa.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.programmergabut.imageharpa.manage.ImageCallback
import com.programmergabut.imageharpa.manage.LoadImageCallback

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
    fun load(callBack: LoadImageCallback)

    /**
     * delete image
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun delete(): Boolean

    /**
     * delete image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun delete(callBack: ImageCallback)

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
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun save(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback)

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
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun save(base64: String, quality: Int, imageCallBack: ImageCallback)

    /**
     * save base64 formatted image
     * @param base64 is the image in base64 string format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun save(drawable: Drawable, quality: Int): Boolean

    /**
     * save drawable formatted image asynchronously
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun save(drawable: Drawable, quality: Int, imageCallBack: ImageCallback)

    /**
     * save drawable formatted image synchronously
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     */
    fun savePublic(bitmap: Bitmap, quality: Int): Boolean

    /**
     * save drawable formatted image synchronously
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun savePublic(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback)

    /**
     * save drawable formatted image synchronously
     * @param base64 is the image in base64 format.
     * @param quality is the quality of the image, must be between 0 and 100.
     */
    fun savePublic(base64: String, quality: Int): Boolean

    /**
     * save drawable formatted image synchronously
     * @param base64 is the image in base64 format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun savePublic(base64: String, quality: Int, imageCallBack: ImageCallback)

    /**
     * save drawable formatted image synchronously
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100.
     */
    fun savePublic(drawable: Drawable, quality: Int): Boolean

    /**
     * save drawable formatted image synchronously
     * @param drawable is the image in drawable format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param imageCallBack is the callback function that will called after process finish.
     */
    fun savePublic(drawable: Drawable, quality: Int, imageCallBack: ImageCallback)

    /**
     * delete image
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun deletePublic(): Boolean

    /**
     * delete image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun deletePublic(callBack: ImageCallback)

    /**
     * get Image URI for sharing
     */
    fun getURI(): Uri?
}

