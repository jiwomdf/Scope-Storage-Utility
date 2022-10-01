package com.programmergabut.androidimageutil.convert.action

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.convert.Base64Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class BitmapToBase64 {

    fun bitToBase64(
        bitmap: Bitmap,
        quality: Int,
        format: Bitmap.CompressFormat,
    ): String? {
        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)
            val byteArray: ByteArray = outputStream.toByteArray()

            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "bitmapToBase64: $ex")
            null
        }
    }

    fun bitToBase64(
        bitmap: Bitmap,
        quality: Int,
        format: Bitmap.CompressFormat,
        callBack: Base64Callback
    ){
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(format, quality, outputStream)
                val byteArray: ByteArray = outputStream.toByteArray()

                val result = Base64.encodeToString(byteArray, Base64.DEFAULT)
                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "bitmapToBase64: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }
}