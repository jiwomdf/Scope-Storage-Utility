package com.programmergabut.androidimageutil.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.programmergabut.androidimageutil.manage.ImageCallback
import com.programmergabut.androidimageutil.manage.LoadImageCallback

interface ManageImage{

    /**
     * load image synchronously
     *  @return null if the file is successfully loaded, and null if the file failed to load
     */
    fun load(): Bitmap?

    /**
     * load image synchronously
     *  @return null if the file is successfully loaded, and null if the file failed to load
     */
    fun loadPublic(): Bitmap?

    /**
     * load image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun load(callBack: LoadImageCallback)

    /**
     * load image asynchronously
     * @param callBack is the callback function that will called after process finish.
     */
    fun loadPublic(callBack: LoadImageCallback)


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
     * get Image URI for sharing
     */
    fun loadPublicUri(): Uri?

    /**
     * delete public image
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun deletePublic(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>): Boolean

    /**
     * delete public image
     * @return true if the file is saved, and false if the file is failed to save
     */
    fun deletePublic(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>, callBack: ImageCallback)

}

