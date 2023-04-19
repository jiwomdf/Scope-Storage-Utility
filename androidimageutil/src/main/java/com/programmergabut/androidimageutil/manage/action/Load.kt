package com.programmergabut.androidimageutil.manage.action

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.action.base.BaseAction
import com.programmergabut.androidimageutil.manage.callback.LoadImageCallback
import com.programmergabut.androidimageutil.util.ErrorMessage
import com.programmergabut.androidimageutil.util.Extension
import com.programmergabut.androidimageutil.util.isUsingScopeStorage
import com.programmergabut.androidimageutil.util.loadBitmapFromUri
import com.programmergabut.androidimageutil.util.loadUriPrivateStorage
import com.programmergabut.androidimageutil.util.loadPublicPhotoUri
import com.programmergabut.androidimageutil.util.loadUriScopeStorage
import com.programmergabut.androidimageutil.util.setExtension
import com.programmergabut.androidimageutil.util.validateDirectory
import com.programmergabut.androidimageutil.util.validateFileName
import com.programmergabut.androidimageutil.util.validateReadPermission
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
    env: String,
    private val toSharedStorage: Boolean
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    private fun loadPrivateBitmap(): Bitmap? {
        validateFileName(fileName)
        validateReadPermission(context)
        val extension = setExtension(fileExtension)
        validateDirectory(directory)

        val file = File(directory, "$fileName$extension")
        return BitmapFactory.decodeFile(file.path)
    }

    private fun loadPublicBitmap(): Bitmap? {
        return if(isUsingScopeStorage){
            val photoUri = loadPublicPhotoUri(context, collection, projection, cleanDirectory, where) ?: throw Exception("cant get photo Uri")
            loadBitmapFromUri(context, photoUri)
        } else {
            val imagePath = externalStoragePublicDir
            validateDirectory(File(imagePath))
            val fileExt = setExtension(fileExtension)
            val file = File(imagePath, "$fileName$fileExt")
            BitmapFactory.decodeFile(file.path)
        }
    }

    fun load(): Bitmap? {
        return try {
            if(toSharedStorage){
                loadPublicBitmap()
            } else {
                loadPrivateBitmap()
            }
        } catch (ex: Exception) {
            Log.e(AndroidImageUtil.TAG, "load: ${ex.message}")
            null
        }
    }

    fun load(callBack: LoadImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bitmap = if(toSharedStorage){
                    loadPublicBitmap()
                } else {
                    loadPrivateBitmap()
                }
                withContext(Dispatchers.Main) { callBack.onResult(bitmap) }
            } catch (ex: Exception) {
                Log.e(AndroidImageUtil.TAG, "load: ${ex.message}")
                withContext(Dispatchers.Main) { callBack.onResult(null) }
            }
        }
    }

    @SuppressLint("NewApi")
    fun loadUri(activity: AppCompatActivity, appId: String): Uri? {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            if(isUsingScopeStorage) {
                collection?.let {
                    loadUriScopeStorage(context, collection, projection, where, cleanDirectory, env) ?: throw Exception("cant get photo Uri")
                } ?: kotlin.run {
                    Log.e(AndroidImageUtil.TAG, "loadPublicUri: collection is null")
                    null
                }
            } else {
                val filePath = externalStoragePublicDir
                val files = File(filePath)
                if(!files.exists()){
                    throw Exception(ErrorMessage.FILE_NOT_FOUND)
                }
                loadUriPrivateStorage(appId, activity, files, fileName, filePath, fileExtension)
            }
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "loadPublicUri: ${ex.message}")
            null
        }
    }

}