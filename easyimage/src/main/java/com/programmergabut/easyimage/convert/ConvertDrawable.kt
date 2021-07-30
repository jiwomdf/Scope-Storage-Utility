package com.programmergabut.easyimage.convert

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

class ConvertDrawable {

    fun base64ToDrawable(base64: String, offset: Int = 0): Drawable {
        if(base64.isEmpty())
            throw IllegalArgumentException("Base64 string cannot be empty")

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            return BitmapDrawable(Resources.getSystem(), bitmap)
        } catch (ex: Exception){
            throw Exception(ex.message.toString())
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap?{
        if(drawable is BitmapDrawable)
            return drawable.bitmap

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

    fun drawableToBase64(drawable: Drawable?): String?{
        if(drawable == null)
            throw IllegalArgumentException("Drawable cannot be null")

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

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}