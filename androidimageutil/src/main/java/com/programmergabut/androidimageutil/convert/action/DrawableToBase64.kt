package com.programmergabut.androidimageutil.convert.action

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.convert.Base64Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class DrawableToBase64{

    fun dToBase64(drawable: Drawable): String? {
        try {
            val convertBitmap: Bitmap = if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else {
                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(convertBitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            val outputStream = ByteArrayOutputStream()
            convertBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray: ByteArray = outputStream.toByteArray()

            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (ex: Exception) {
            Log.e(AndroidImageUtil.TAG, "drawableToBase64: $ex")
            return null
        }
    }

    fun dToBase64(
        drawable: Drawable,
        callBack: Base64Callback
    ){
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val convertBitmap: Bitmap = if(drawable is BitmapDrawable){
                    drawable.bitmap
                } else {
                    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
                    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                }

                val canvas = Canvas(convertBitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                val outputStream = ByteArrayOutputStream()
                convertBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
                val byteArray: ByteArray = outputStream.toByteArray()
                val result = Base64.encodeToString(byteArray, Base64.DEFAULT)

                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "drawableToBase64: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }
}