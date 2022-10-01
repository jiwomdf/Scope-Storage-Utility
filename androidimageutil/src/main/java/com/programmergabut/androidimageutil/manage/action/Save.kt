package com.programmergabut.androidimageutil.manage.action

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.ImageCallback
import com.programmergabut.androidimageutil.manage.action.base.BaseAction
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
    env: String = Environment.DIRECTORY_PICTURES
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    fun s(bitmap: Bitmap, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateStoragePermission(context)
            val extension = setExtension(fileExtension)
            val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
            val directory = getOrCreateDirectoryIfEmpty(expectedDir)

            val file = File(directory, "$fileName$extension")
            deleteFileIfExist(file)
            val outputStream = FileOutputStream(file)
            compressBitmap(outputStream, bitmap, quality, fileExtension)
            true
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun s(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateImageQuality(quality)
                validateStoragePermission(context)
                val extension = setExtension(fileExtension)
                val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
                val directory = getOrCreateDirectoryIfEmpty(expectedDir)

                val file = File(directory, "$fileName$extension")
                deleteFileIfExist(file)
                runCatching {
                    val outputStream = FileOutputStream(file)
                    compressBitmap(outputStream, bitmap, quality, fileExtension)
                }
                withContext(Dispatchers.Main){
                    imageCallBack.onSuccess()
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }

    }

    fun s(base64: String, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateBase64String(base64)
            validateStoragePermission(context)
            val bitmap = decodeByteArray(base64)
            val extension = setExtension(fileExtension)
            val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
            val directory = getOrCreateDirectoryIfEmpty(expectedDir)

            val file = File(directory, "$fileName$extension")
            deleteFileIfExist(file)
            val outputStream = FileOutputStream(file)
            compressBitmap(outputStream, bitmap, quality, fileExtension)
            true
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun s(base64: String, quality: Int, imageCallBack: ImageCallback){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                validateImageQuality(quality)
                validateBase64String(base64)
                validateStoragePermission(context)
                val bitmap = decodeByteArray(base64)
                val extension = setExtension(fileExtension)
                val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
                val directory = getOrCreateDirectoryIfEmpty(expectedDir)

                val file = File(directory, "$fileName$extension")
                deleteFileIfExist(file)
                runCatching {
                    val outputStream = FileOutputStream(file)
                    compressBitmap(outputStream, bitmap, quality, fileExtension)
                }
                withContext(Dispatchers.Main){
                    imageCallBack.onSuccess()
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }
    }

    fun s(drawable: Drawable, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateStoragePermission(context)
            val bitmap = drawableToBitmap(drawable)
            val extension = setExtension(fileExtension)
            val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
            val directory = getOrCreateDirectoryIfEmpty(expectedDir)

            val file = File(directory, "$fileName$extension")
            deleteFileIfExist(file)
            val outputStream = FileOutputStream(file)
            compressBitmap(outputStream, bitmap, quality, fileExtension)
            true
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun s(drawable: Drawable, quality: Int,
             imageCallBack: ImageCallback
    ){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateImageQuality(quality)
                validateStoragePermission(context)
                val bitmap = drawableToBitmap(drawable)
                val extension = setExtension(fileExtension)
                val expectedDir = File("${absolutePath}${File.separator}$finalDirectory")
                val directory = getOrCreateDirectoryIfEmpty(expectedDir)

                val file = File(directory, "$fileName$extension")
                deleteFileIfExist(file)
                runCatching {
                    val outputStream = FileOutputStream(file)
                    compressBitmap(outputStream, bitmap, quality, fileExtension)
                }
                withContext(Dispatchers.Main){
                    imageCallBack.onSuccess()
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }
    }

    fun sPublic(bitmap: Bitmap, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateStoragePermission(context)
            val env = Environment.DIRECTORY_DCIM
            val directory = "${env}${File.separator}$finalDirectory"
            val expectedDir = File(directory)
            getOrCreateDirectoryIfEmpty(expectedDir)
            deleteExistingPublicImage(context, collection, projection, where)
            savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun sPublic(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateImageQuality(quality)
                validateStoragePermission(context)
                val directory = "${Environment.DIRECTORY_DCIM}${File.separator}$finalDirectory"
                val expectedDir = File(directory)
                getOrCreateDirectoryIfEmpty(expectedDir)
                deleteExistingPublicImage(context, collection, projection, where)
                val isSuccess = savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
                withContext(Dispatchers.Main){
                    if(isSuccess) {
                        imageCallBack.onSuccess()
                    } else {
                        imageCallBack.onFailed(Exception())
                    }
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }
    }

    fun sPublic(base64: String, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateBase64String(base64)
            validateStoragePermission(context)
            val bitmap = decodeByteArray(base64)
            val directory = "${Environment.DIRECTORY_DCIM}${File.separator}$finalDirectory${File.separator}"
            val expectedDir = File(directory)
            getOrCreateDirectoryIfEmpty(expectedDir)
            deleteExistingPublicImage(context, collection, projection, where)
            savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun sPublic(base64: String, quality: Int, imageCallBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateImageQuality(quality)
                validateBase64String(base64)
                validateStoragePermission(context)
                val bitmap = decodeByteArray(base64)
                val directory = "${Environment.DIRECTORY_DCIM}${File.separator}$finalDirectory${File.separator}"
                val expectedDir = File(directory)
                getOrCreateDirectoryIfEmpty(expectedDir)
                deleteExistingPublicImage(context, collection, projection, where)
                val isSuccess = savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
                withContext(Dispatchers.Main){
                    if(isSuccess) {
                        imageCallBack.onSuccess()
                    } else {
                        imageCallBack.onFailed(Exception())
                    }
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }
    }

    fun sPublic(drawable: Drawable, quality: Int): Boolean {
        return try {
            validateImageQuality(quality)
            validateStoragePermission(context)
            val directory = "${Environment.DIRECTORY_DCIM}${File.separator}$finalDirectory${File.separator}"
            val expectedDir = File(directory)
            getOrCreateDirectoryIfEmpty(expectedDir)
            deleteExistingPublicImage(context, collection, projection, where)
            val bitmap = drawableToBitmap(drawable)
            savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
            false
        }
    }

    fun sPublic(drawable: Drawable, quality: Int, imageCallBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateImageQuality(quality)
                validateStoragePermission(context)
                val directory = "${Environment.DIRECTORY_DCIM}${File.separator}$finalDirectory${File.separator}"
                val expectedDir = File(directory)
                getOrCreateDirectoryIfEmpty(expectedDir)
                deleteExistingPublicImage(context, collection, projection, where)
                val bitmap = drawableToBitmap(drawable)
                val isSuccess = savePublicImage(context, bitmap, directory, quality, fileName, fileExtension)
                withContext(Dispatchers.Main){
                    if(isSuccess) {
                        imageCallBack.onSuccess()
                    } else {
                        imageCallBack.onFailed(Exception())
                    }
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "save: ${ex.message}")
                withContext(Dispatchers.Main){
                    imageCallBack.onFailed(ex)
                }
            }
        }
    }
}