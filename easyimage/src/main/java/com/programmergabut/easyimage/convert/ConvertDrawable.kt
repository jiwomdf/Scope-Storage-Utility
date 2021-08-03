package com.programmergabut.easyimage.convert

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.programmergabut.easyimage.Event
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

class ConvertDrawable {

    private var _bitmapToDrawable = MutableLiveData<Event<Drawable>>()
    val bitmapToDrawable: LiveData<Event<Drawable>> = _bitmapToDrawable
    fun base64ToDrawable(base64: String, offset: Int = 0) {
        _bitmapToDrawable.postValue(Event.Loading)
        if(base64.isEmpty()){
            _bitmapToDrawable.postValue(Event.Error("Base64 string cannot be empty"))
            return
        }

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            val result = BitmapDrawable(Resources.getSystem(), bitmap)

            _bitmapToDrawable.postValue(Event.Success(result))
        } catch (ex: Exception){
            _bitmapToDrawable.postValue(Event.Error(ex.message.toString()))
        }
    }

    private var _drawableToBitmap = MutableLiveData<Event<Bitmap>>()
    val drawableToBitmap: LiveData<Event<Bitmap>> = _drawableToBitmap
    fun drawableToBitmap(drawable: Drawable) {
        _drawableToBitmap.postValue(Event.Loading)
        if(drawable is BitmapDrawable){
            _drawableToBitmap.postValue(Event.Success(drawable.bitmap))
            return
        }

        try {
            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            _drawableToBitmap.postValue(Event.Success(bitmap))
        } catch (ex: Exception) {
            _drawableToBitmap.postValue(Event.Error(ex.message.toString()))
        }
    }


    private var _drawableToBase64 = MutableLiveData<Event<String>>()
    val drawableToBase64: LiveData<Event<String>> = _drawableToBase64
    fun drawableToBase64(drawable: Drawable?){
        if(drawable == null){
            _drawableToBase64.postValue(Event.Error("Drawable cannot be null"))
            return
        }

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

            _drawableToBase64.postValue(Event.Success(result))
        } catch (ex: Exception){
            _drawableToBase64.postValue(Event.Error(ex.message.toString()))
        }

    }
}