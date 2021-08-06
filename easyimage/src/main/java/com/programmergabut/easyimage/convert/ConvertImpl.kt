package com.programmergabut.easyimage.convert

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
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
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        val byteArray: ByteArray = outputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun bitmapToBase64(
        bitmap: Bitmap,
        quality: Int,
        format: Bitmap.CompressFormat,
        callBack: IConvertBitmap.Base64CallBack
    ){
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(format, quality, outputStream)
                val byteArray: ByteArray = outputStream.toByteArray()

                val result = Base64.encodeToString(byteArray, Base64.DEFAULT)
                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                withContext(Dispatchers.Main){ callBack.onFailed(ex.message.toString()) }
            }
        }
    }

    override fun base64ToBitmap(base64: String, offset: Int): Bitmap? {
        if(base64.isEmpty()){
            throw InvalidParameterException("Base64 string cannot be empty")
        }

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
        } catch (ex: Exception) {
            throw Exception(ex.message.toString())
        }
    }

    override fun base64ToBitmap(
        base64: String,
        offset: Int,
        callBack: IConvertBitmap.BitmapCallBack
    ){
        CoroutineScope(Dispatchers.Default).launch {
            if(base64.isEmpty()){
                withContext(Dispatchers.Main){
                    callBack.onFailed("Base64 string cannot be empty")
                }
            }

            try {
                val byteArray = Base64.decode(base64, Base64.DEFAULT)
                val result = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                withContext(Dispatchers.Main){ callBack.onFailed(ex.message.toString()) }
            }
        }
    }

    override fun base64ToDrawable(
        base64: String,
        offset: Int
    ): Drawable {
        if(base64.isEmpty()){
            throw InvalidParameterException("Base64 string cannot be empty")
        }

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            return BitmapDrawable(Resources.getSystem(), bitmap)
        } catch (ex: Exception) {
            throw Exception(ex.message.toString())
        }
    }

    override fun base64ToDrawable(
        base64: String,
        offset: Int,
        callBack: IConvertBitmap.DrawableCallBack
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            if(base64.isEmpty()){
                withContext(Dispatchers.Main) {
                    callBack.onFailed("Base64 string cannot be empty")
                }
                return@launch
            }

            try {
                val byteArray = Base64.decode(base64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
                val result = BitmapDrawable(Resources.getSystem(), bitmap)

                withContext(Dispatchers.Main){ callBack.onResult(result) }
            } catch (ex: Exception){
                callBack.onFailed(ex.message.toString())
            }
        }
    }

    override fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if(drawable is BitmapDrawable){
            return drawable.bitmap
        }

        try {
            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        } catch (ex: Exception) {
            throw Exception(ex.message.toString())
        }
    }

    override fun drawableToBitmap(
        drawable: Drawable,
        callBack: IConvertBitmap.BitmapCallBack
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            if(drawable is BitmapDrawable){
                withContext(Dispatchers.Main){
                    callBack.onResult(drawable.bitmap)
                }
                return@launch
            }

            try {
                val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
                val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                withContext(Dispatchers.Main){ callBack.onResult(bitmap) }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main){ callBack.onFailed(ex.message.toString()) }
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
            throw Exception(ex.message.toString())
        }
    }

    override fun drawableToBase64(
        drawable: Drawable,
        callBack: IConvertBitmap.Base64CallBack
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
                withContext(Dispatchers.Main){ callBack.onFailed(ex.message.toString()) }
            }
        }
    }
}