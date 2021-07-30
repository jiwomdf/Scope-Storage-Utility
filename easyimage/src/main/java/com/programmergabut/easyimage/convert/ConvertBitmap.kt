package com.programmergabut.easyimage.convert

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

class ConvertBitmap {

    fun bitmapToBase64(bitmap: Bitmap,
                       quality: Int = 100,
                       format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): String{
        try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)
            val byteArray: ByteArray = outputStream.toByteArray()

            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (ex: Exception){
            throw Exception(ex.message.toString())
        }
    }

    fun base64ToBitmap(base64: String, offset: Int = 0): Bitmap {
        if(base64.isEmpty())
            throw IllegalArgumentException("Base64 string cannot be empty")

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
        } catch (ex: Exception){
            throw Exception(ex.message.toString())
        }
    }
}