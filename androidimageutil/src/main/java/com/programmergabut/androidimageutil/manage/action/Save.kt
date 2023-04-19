package com.programmergabut.androidimageutil.manage.action

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.callback.ImageCallback
import com.programmergabut.androidimageutil.manage.action.base.BaseAction
import com.programmergabut.androidimageutil.manage.callback.OutStreamCallback
import com.programmergabut.androidimageutil.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class Save(
    context: Context,
    fileName: String,
    directory: String?,
    fileExtension: Extension,
    env: String,
    private val toSharedStorage: Boolean
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    private fun savePrivate(quality: Int, bitmap: Bitmap) {
        validateImageQuality(quality)
        validateStoragePermission(context)
        val extension = setExtension(fileExtension)
        val directory = getOrCreateDirectoryIfEmpty(directory)
        val file = File(directory, "$fileName$extension")
        deleteFileIfExist(file)
        runCatching {
            val outputStream = FileOutputStream(file)
            compressBitmap(outputStream, bitmap, quality, fileExtension)
        }
    }

    private fun savePublic(quality: Int, bitmap: Bitmap) {
        validateImageQuality(quality)
        validateStoragePermission(context)
        deleteExistingPublicImage(context, collection, projection, cleanDirectory, where)
        val outputStream = getOutStream(context, externalStorageDirectory, fileName, fileExtension, env)
        compressBitmap(outputStream, bitmap, quality, fileExtension)
    }

    fun save(bitmap: Bitmap, quality: Int): Boolean {
        return try {
            if(toSharedStorage){
                savePublic(quality, bitmap)
            } else {
                savePrivate(quality, bitmap)
            }
            true
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun save(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(isUsingScopeStorage){
                    savePublic(quality, bitmap)
                } else {
                    savePrivate(quality, bitmap)
                }
                imageCallBack.onSuccess()
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                imageCallBack.onFailed(ex)
            }
        }
    }

    fun getRawOutStream(outStreamCallback: OutStreamCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateStoragePermission(context)
                deleteExistingPublicImage(context, collection, projection, cleanDirectory, where)
                val outputStream = getOutStream(
                    context,
                    externalStorageDirectory,
                    fileName,
                    fileExtension,
                    env
                )
                withContext(Dispatchers.Main){
                    outStreamCallback.onSuccess(outputStream)
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){ outStreamCallback.onFailed(ex) }
            }
        }
    }
}