package com.programmergabut.easyimage.domain

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.manage.IManageImage
import com.programmergabut.easyimage.setExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException

interface ManageImage{

    fun load(callBack: IManageImage.LoadCallBack)

    fun delete(callBack: IManageImage.DeleteCallBack)

    fun save(bitmap: Bitmap, quality: Int, callBack: IManageImage.SaveBitMapCallBack)

    fun save(base64: String, quality: Int, callBack: IManageImage.SaveBase64CallBack)

    fun save(drawable: Drawable, quality: Int, callBack: IManageImage.SaveDrawableCallBack)

}

