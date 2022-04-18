package com.programmergabut.androidimageutil.convert

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.security.InvalidParameterException

class ConvertImpl: Convert {

    override fun bitmapToBase64(
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
            Log.e(TAG, "bitmapToBase64: $ex")
            null
        }
    }

    override fun bitmapToBase64(
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
                Log.e(TAG, "bitmapToBase64: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }

    override fun base64ToBitmap(base64: String, offset: Int): Bitmap? {
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
            Log.e(TAG, "base64ToBitmap: $ex")
            null
        }
    }

    override fun base64ToBitmap(
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
                Log.e(TAG, "base64ToBitmap: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }

    override fun base64ToDrawable(
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
            Log.e(TAG, "base64ToDrawable: $ex")
            null
        }
    }

    override fun base64ToDrawable(
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
                Log.e(TAG, "base64ToDrawable: $ex")
                callBack.onFailed(ex)
            }
        }
    }

    override fun drawableToBitmap(drawable: Drawable): Bitmap? {
        try {
            if(drawable is BitmapDrawable){
                return drawable.bitmap
            }

            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        } catch (ex: Exception) {
            Log.e(TAG, "drawableToBitmap: $ex")
            return null
        }
    }

    override fun drawableToBitmap(
        drawable: Drawable,
        callBack: BitmapCallback
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                if(drawable is BitmapDrawable){
                    withContext(Dispatchers.Main){
                        callBack.onResult(drawable.bitmap)
                    }
                    return@launch
                }


                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                withContext(Dispatchers.Main){ callBack.onResult(bitmap) }
            } catch (ex: Exception) {
                Log.e(TAG, "drawableToBitmap: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }


    override fun drawableToBase64(drawable: Drawable): String? {
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
            Log.e(TAG, "drawableToBase64: $ex")
            return null
        }
    }

    override fun drawableToBase64(
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
                Log.e(TAG, "drawableToBase64: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }
}