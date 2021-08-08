package com.programmergabut.easyimage.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.io.IOException

interface Convert {

    /**
     * convert bitmap to base64
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param format is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
     * @return string of base64 if success, or null if fail
     */
    fun bitmapToBase64(bitmap: Bitmap, quality: Int,
                       format: Bitmap.CompressFormat
    ): String?

    /**
     * convert bitmap to base64 asynchronously
     * @param bitmap is the image in bitmap format.
     * @param quality is the quality of the image, must be between 0 and 100.
     * @param format is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
     * @param callBack is the callback function that will called after process finish.
     */
    fun bitmapToBase64(bitmap: Bitmap, quality: Int,
                       format: Bitmap.CompressFormat, callBack: IConvertBitmap.Base64CallBack
    )

    /**
     * convert base64 to Bitmap
     * @param base64 is the image in base64 string format.
     * @param offset is a number of values to skip before the first color in the array of colors.
     * @return bitmap if success, or null if fail
     */
    fun base64ToBitmap(base64: String, offset: Int): Bitmap?

    /**
     * convert base64 to Bitmap asynchronously
     * @param base64 is the image in base64 string format.
     * @param offset is a number of values to skip before the first color in the array of colors.
     * @param callBack is the callback function that will called after process finish.
     */
    fun base64ToBitmap(base64: String, offset: Int,
                       callBack: IConvertBitmap.BitmapCallBack
    )

    /**
     * convert base64 to Drawable
     * @param base64 is the image in base64 string format.
     * @param offset is a number of values to skip before the first color in the array of colors.
     * @return drawable if success, or null if fail
     */
    fun base64ToDrawable(base64: String, offset: Int) : Drawable?

    /**
     * convert base64 to Drawable asynchronously
     * @param base64 is the image in base64 string format.
     * @param offset is a number of values to skip before the first color in the array of colors.
     * @param callBack is the callback function that will called after process finish.
     */
    fun base64ToDrawable(base64: String, offset: Int,
                         callBack: IConvertBitmap.DrawableCallBack
    )

    /**
     * convert drawable to Bitmap
     * @param drawable is the image in drawable.
     * @return bitmap if success, or null if fail
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap?

    /**
     * convert drawable to Bitmap asynchronously
     * @param drawable is the image in drawable.
     * @param callBack is the callback function that will called after process finish.
     */
    fun drawableToBitmap(drawable: Drawable,
                         callBack: IConvertBitmap.BitmapCallBack
    )

    /**
     * convert drawable to base64
     * @param drawable is the image in drawable.
     * @return base64 string if success, or null if fail
     */
    fun drawableToBase64(drawable: Drawable): String?

    /**
     * convert drawable to base64 asynchronously
     * @param drawable is the image in drawable.
     * @param callBack is the callback function that will called after process finish.
     */
    fun drawableToBase64(drawable: Drawable,
                         callBack: IConvertBitmap.Base64CallBack
    )
}