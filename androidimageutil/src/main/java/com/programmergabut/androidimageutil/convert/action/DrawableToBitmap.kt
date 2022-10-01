package com.programmergabut.androidimageutil.convert.action

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.convert.BitmapCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawableToBitmap {

    fun dToBitmap(drawable: Drawable): Bitmap? {
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
            Log.e(AndroidImageUtil.TAG, "drawableToBitmap: $ex")
            return null
        }
    }

    fun dToBitmap(
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
                Log.e(AndroidImageUtil.TAG, "drawableToBitmap: $ex")
                withContext(Dispatchers.Main){ callBack.onFailed(ex) }
            }
        }
    }
}