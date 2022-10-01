package com.programmergabut.androidimageutil.convert.action

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.convert.BitmapCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class Base64ToBitmap {

    fun b64ToBitmap(base64: String, offset: Int): Bitmap? {
        return try {
            if(base64.isEmpty())
                throw InvalidParameterException("Base64 string cannot be empty")

            val cleanImage: String = base64
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "")

            val byteArray = Base64.decode(cleanImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            bitmap
        } catch (ex: Exception) {
            Log.e(AndroidImageUtil.TAG, "base64ToBitmap: $ex")
            null
        }
    }

    fun b64ToBitmap(
        base64: String,
        offset: Int,
        callBack: BitmapCallback
    ){
        CoroutineScope(Dispatchers.Default).launch {
            try {
                if(base64.isEmpty())
                    throw Exception("Base64 string cannot be empty")

                val cleanImage: String = base64
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "")

                val byteArray = Base64.decode(cleanImage, Base64.DEFAULT)
                val result = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "base64ToBitmap: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }
}