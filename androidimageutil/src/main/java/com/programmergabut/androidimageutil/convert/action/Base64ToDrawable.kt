package com.programmergabut.androidimageutil.convert.action

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.convert.Convert
import com.programmergabut.androidimageutil.convert.DrawableCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.InvalidParameterException

class Base64ToDrawable{

    fun b64ToDrawable(
        base64: String,
        offset: Int
    ): Drawable? {
        return try {
            if(base64.isEmpty())
                throw InvalidParameterException("Base64 string cannot be empty")

            val cleanImage: String = base64
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,", "")

            val byteArray = Base64.decode(cleanImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            BitmapDrawable(Resources.getSystem(), bitmap)
        } catch (ex: Exception) {
            Log.e(AndroidImageUtil.TAG, "base64ToDrawable: $ex")
            null
        }
    }

    fun b64ToDrawable(
        base64: String,
        offset: Int,
        callBack: DrawableCallback
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                if(base64.isEmpty())
                    throw Exception("Base64 string cannot be empty")

                val cleanImage: String = base64
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "")

                val byteArray = Base64.decode(cleanImage, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
                val result = BitmapDrawable(Resources.getSystem(), bitmap)

                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "base64ToDrawable: $ex")
                callBack.onFailed(ex)
            }
        }
    }

}