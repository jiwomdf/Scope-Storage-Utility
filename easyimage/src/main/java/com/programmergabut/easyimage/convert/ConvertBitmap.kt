package com.programmergabut.easyimage.convert

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.programmergabut.easyimage.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

class ConvertBitmap {

    private var _bitmapToBase64 = MutableLiveData<Event<String>>()
    val bitmapToBase64: LiveData<Event<String>> = _bitmapToBase64
    fun bitmapToBase64(bitmap: Bitmap,
                       quality: Int = 100,
                       format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ) = CoroutineScope(Dispatchers.Default).launch {
            try {
                _bitmapToBase64.postValue(Event.Loading)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(format, quality, outputStream)
                val byteArray: ByteArray = outputStream.toByteArray()

                val result = Base64.encodeToString(byteArray, Base64.DEFAULT)
                _bitmapToBase64.postValue(Event.Success(result))
            } catch (ex: Exception){
                _bitmapToBase64.postValue(Event.Error(ex.message.toString()))
            }
        }


    private var _base64ToBitmap = MutableLiveData<Event<Bitmap>>()
    val base64ToBitmap: LiveData<Event<Bitmap>> = _base64ToBitmap
    fun base64ToBitmap(base64: String, offset: Int = 0)
    = CoroutineScope(Dispatchers.Default).launch {
        _bitmapToBase64.postValue(Event.Loading)

        if(base64.isEmpty())
            _bitmapToBase64.postValue(Event.Error("Base64 string cannot be empty"))

        try {
            val byteArray = Base64.decode(base64, Base64.DEFAULT)
            val result = BitmapFactory.decodeByteArray(byteArray, offset, byteArray.size)
            _base64ToBitmap.postValue(Event.Success(result))
        } catch (ex: Exception){
            _base64ToBitmap.postValue(Event.Error(ex.message.toString()))
        }
    }
}