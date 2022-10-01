package com.programmergabut.androidimageutil.manage.action

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.LoadImageCallback
import com.programmergabut.androidimageutil.manage.action.base.BaseAction
import com.programmergabut.androidimageutil.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Load(
    context: Context,
    fileName: String,
    directory: String?,
    fileExtension: Extension,
    env: String = Environment.DIRECTORY_PICTURES
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    fun l(): Bitmap? {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            val extension = setExtension(fileExtension)
            val directory = File("${absolutePath}${File.separator}$finalDirectory")
            validateDirectory(directory)

            val file = File(directory, "$fileName$extension")
            BitmapFactory.decodeFile(file.path)
        } catch (ex: Exception) {
            Log.e(AndroidImageUtil.TAG, "load: ${ex.message}")
            null
        }
    }

    fun l(callBack: LoadImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateFileName(fileName)
                validateReadPermission(context)
                val extension = setExtension(fileExtension)
                val directory = File("${absolutePath}${File.separator}$finalDirectory")
                validateDirectory(directory)

                val file = File(directory, "$fileName$extension")
                val result = BitmapFactory.decodeFile(file.path)
                withContext(Dispatchers.Main) {
                    callBack.onResult(result)
                }
            } catch (ex: Exception) {
                Log.e(AndroidImageUtil.TAG, "load: ${ex.message}")
                withContext(Dispatchers.Main) {
                    callBack.onResult(null)
                }
            }
        }
    }

    fun lPublic(): Bitmap? {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            val photoUri = loadPublicPhotoUri(context, collection, projection, where) ?: throw Exception("cant get photo Uri")
            loadBitmapFromUri(context, photoUri)
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "loadPublic: ${ex.message}", )
            null
        }
    }

    fun lPublic(callBack: LoadImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateFileName(fileName)
                validateReadPermission(context)
                val photoUri = loadPublicPhotoUri(context, collection, projection, where) ?: throw Exception("cant get photo Uri")
                val bitmap = loadBitmapFromUri(context, photoUri)
                withContext(Dispatchers.Main){
                    callBack.onResult(bitmap)
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "loadPublic: ${ex.message}", )
                withContext(Dispatchers.Main){
                    callBack.onFailed(ex)
                }
            }
        }
    }

    fun lPublicUri(): Uri? {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            loadPublicPhotoUri(context, collection, projection, where) ?: throw Exception("cant get photo Uri")
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "loadPublicUri: ${ex.message}", )
            null
        }
    }

}