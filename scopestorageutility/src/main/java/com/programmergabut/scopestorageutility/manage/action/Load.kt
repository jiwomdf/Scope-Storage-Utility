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
import com.programmergabut.scopestorageutility.util.imageutil.isUsingScopeStorage
import com.programmergabut.scopestorageutility.util.imageutil.loadBitmapFromUri
import com.programmergabut.scopestorageutility.util.imageutil.loadSharedUri
import com.programmergabut.scopestorageutility.util.imageutil.loadUriPrivateStorage
import com.programmergabut.scopestorageutility.util.imageutil.loadUriScopeStorage
import com.programmergabut.scopestorageutility.util.imageutil.validateDirectory
import com.programmergabut.scopestorageutility.util.imageutil.validateFileName
import com.programmergabut.scopestorageutility.util.imageutil.validateReadPermission
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

    private fun loadSharedBitmapNonScopeStorage(): Bitmap? {
        val imagePath = externalStorageSharedDir
        validateDirectory(File(imagePath))
        val file = File(imagePath, "$fileName${fileExtension.extension}")
        return BitmapFactory.decodeFile(file.path)
    }

    private fun loadSharedBitmapScopeStorage(): Bitmap? {
        val photoUri = loadSharedUri(context, collection, projection, cleanDirectory, where)
            ?: throw Exception(ErrorMessage.CANT_GET_PHOTO_URI)
        return loadBitmapFromUri(context, photoUri)
    }

    fun load(): Bitmap? {
        return try {
            if(toSharedStorage){
                if(isUsingScopeStorage) {
                    loadSharedBitmapScopeStorage()
                } else {
                    loadSharedBitmapNonScopeStorage()
                }
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
                    if(isUsingScopeStorage) {
                        loadSharedBitmapScopeStorage()
                    } else {
                        loadSharedBitmapNonScopeStorage()
                    }
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
    fun loadSharedFileUri(activity: AppCompatActivity, appId: String): Uri? {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            if(isUsingScopeStorage) {
                collection?.let {
                    loadUriScopeStorage(context, collection, projection, where, cleanDirectory, env) ?: throw Exception(ErrorMessage.CANT_GET_PHOTO_URI)
                } ?: kotlin.run {
                    Log.e(ScopeStorageUtility.TAG, "loadSharedUri: collection is null")
                    null
                }
            } else {
                val files = File(externalStorageSharedDir)
                if(!files.exists()){
                    throw Exception(ErrorMessage.FILE_NOT_FOUND)
                }
                loadUriPrivateStorage(appId, activity, fileName, externalStorageSharedDir, fileExtension)
            }
        } catch (ex: Exception){
            Log.e(ScopeStorageUtility.TAG, "loadSharedUri: ${ex.message}")
            null
        }
    }

}