package com.programmergabut.scopestorageutility.manage.action

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.programmergabut.scopestorageutility.ScopeStorageUtility
import com.programmergabut.scopestorageutility.manage.action.base.BaseAction
import com.programmergabut.scopestorageutility.manage.callback.LoadImageCallback
import com.programmergabut.scopestorageutility.util.ErrorMessage
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.isUsingScopeStorage
import com.programmergabut.scopestorageutility.util.loadBitmapFromUri
import com.programmergabut.scopestorageutility.util.loadPublicUri
import com.programmergabut.scopestorageutility.util.loadUriPrivateStorage
import com.programmergabut.scopestorageutility.util.loadUriScopeStorage
import com.programmergabut.scopestorageutility.util.validateDirectory
import com.programmergabut.scopestorageutility.util.validateFileName
import com.programmergabut.scopestorageutility.util.validateReadPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Load(
    context: Context,
    fileName: String,
    directory: String?,
    fileExtension: Extension.ExtensionModel,
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
        validateDirectory(directory)

        val file = File(directory, "$fileName${fileExtension.extension}")
        return BitmapFactory.decodeFile(file.path)
    }

    private fun loadPublicBitmap(): Bitmap? {
        return if(isUsingScopeStorage){
            val photoUri = loadPublicUri(context, collection, projection, cleanDirectory, where)
                ?: throw Exception(ErrorMessage.CANT_GET_PHOTO_URI)
            loadBitmapFromUri(context, photoUri)
        } else {
            val imagePath = externalStoragePublicDir
            validateDirectory(File(imagePath))
            val file = File(imagePath, "$fileName${fileExtension.extension}")
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
            Log.e(ScopeStorageUtility.TAG, "load: ${ex.message}")
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
                Log.e(ScopeStorageUtility.TAG, "load: ${ex.message}")
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
                    loadUriScopeStorage(context, collection, projection, where, cleanDirectory, env) ?: throw Exception(ErrorMessage.CANT_GET_PHOTO_URI)
                } ?: kotlin.run {
                    Log.e(ScopeStorageUtility.TAG, "loadPublicUri: collection is null")
                    null
                }
            } else {
                val files = File(externalStoragePublicDir)
                if(!files.exists()){
                    throw Exception(ErrorMessage.FILE_NOT_FOUND)
                }
                loadUriPrivateStorage(appId, activity, fileName, externalStoragePublicDir, fileExtension)
            }
        } catch (ex: Exception){
            Log.e(ScopeStorageUtility.TAG, "loadPublicUri: ${ex.message}")
            null
        }
    }

}