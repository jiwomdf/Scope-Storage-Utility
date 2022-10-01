package com.programmergabut.androidimageutil.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.programmergabut.androidimageutil.convert.action.*

open class Convert {

    /**
     * Bitmap to Base64 Region
     */
    private val bitmapToBase64 = BitmapToBase64()
    fun bitmapToBase64(bitmap: Bitmap, quality: Int, format: Bitmap.CompressFormat): String? =
        bitmapToBase64.bitToBase64(bitmap, quality, format)
    fun bitmapToBase64(bitmap: Bitmap, quality: Int, format: Bitmap.CompressFormat, callBack: Base64Callback){
        bitmapToBase64.bitToBase64(bitmap, quality, format, object: Base64Callback {
            override fun onResult(base64: String) {callBack.onResult(base64)}
            override fun onFailed(ex: Exception) {callBack.onFailed(ex)}
        })
    }
    fun bitmapToBase64(
        bitmap: Bitmap,
        quality: Int,
        format: Bitmap.CompressFormat,
        block: (base64: String) -> Unit,
        catch: ((e: Exception) -> Unit)? = null,
    ){
        bitmapToBase64.bitToBase64(bitmap, quality, format, object: Base64Callback {
            override fun onResult(base64: String) {block.invoke(base64)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }

    /**
     *  Base64 to Bitmap Region
     */
    private val base64ToBitmap = Base64ToBitmap()
    fun base64ToBitmap(base64: String, offset: Int): Bitmap? = base64ToBitmap.b64ToBitmap(base64, offset)
    fun base64ToBitmap(base64: String, offset: Int, callBack: BitmapCallback){
        base64ToBitmap.b64ToBitmap(base64, offset, object: BitmapCallback {
            override fun onResult(bitmap: Bitmap) {callBack.onResult(bitmap)}
            override fun onFailed(ex: Exception) {callBack.onFailed(ex)}
        })
    }
    fun base64ToBitmap(
        base64: String,
        offset: Int,
        block: (bitmap: Bitmap) -> Unit,
        catch: ((e: Exception) -> Unit)? = null){
        base64ToBitmap.b64ToBitmap(base64, offset, object: BitmapCallback {
            override fun onResult(bitmap: Bitmap) {block.invoke(bitmap)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }

    /**
     * Bitmap to Drawable Region
     */
    private val base64ToDrawable = Base64ToDrawable()
    fun base64ToDrawable(base64: String, offset: Int): Drawable? = base64ToDrawable.b64ToDrawable(base64, offset)
    fun base64ToDrawable(base64: String, offset: Int, callBack: DrawableCallback) {
        base64ToDrawable.b64ToDrawable(base64, offset, object: DrawableCallback {
            override fun onResult(drawable: Drawable) {callBack.onResult(drawable)}
            override fun onFailed(ex: Exception) {callBack.onFailed(ex)}
        })
    }
    fun base64ToDrawable(
        base64: String, offset: Int,
        block: (drawable: Drawable) -> Unit,
        catch: ((e: Exception) -> Unit)? = null
    ) {
        base64ToDrawable.b64ToDrawable(base64, offset, object: DrawableCallback {
            override fun onResult(drawable: Drawable) {block.invoke(drawable)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }

    /**
     * Drawable to Bitmap Region
     */
    private val drawableToBitmap = DrawableToBitmap()
    fun drawableToBitmap(drawable: Drawable): Bitmap? = drawableToBitmap.dToBitmap(drawable)
    fun drawableToBitmap(drawable: Drawable, callBack: BitmapCallback) {
        drawableToBitmap.dToBitmap(drawable, object: BitmapCallback {
            override fun onResult(bitmap: Bitmap) {callBack.onResult(bitmap)}
            override fun onFailed(ex: Exception) {callBack.onFailed(ex)}
        })
    }
    fun drawableToBitmap(
        drawable: Drawable,
        block: (bitmap: Bitmap) -> Unit,
        catch: ((e: Exception) -> Unit)? = null
    ) {
        drawableToBitmap.dToBitmap(drawable, object: BitmapCallback {
            override fun onResult(bitmap: Bitmap) {block.invoke(bitmap)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }


    /**
     * Drawable to Bitmap Region
     */
    private val drawableToBase64 = DrawableToBase64()
    fun drawableToBase64(drawable: Drawable): String? = drawableToBase64.dToBase64(drawable)
    fun drawableToBase64(drawable: Drawable, callBack: Base64Callback){
        drawableToBase64.dToBase64(drawable, object: Base64Callback {
            override fun onResult(base64: String) {callBack.onResult(base64)}
            override fun onFailed(ex: Exception) {callBack.onFailed(ex)}
        })
    }
    fun drawableToBase64(
        drawable: Drawable,
        block: (base64: String) -> Unit,
        catch: ((e: Exception) -> Unit)? = null
    ){
        drawableToBase64.dToBase64(drawable, object: Base64Callback {
            override fun onResult(base64: String) {block.invoke(base64)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
}