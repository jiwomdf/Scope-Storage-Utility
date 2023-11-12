package com.programmergabut.scopestorageutility.manage.action

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.programmergabut.scopestorageutility.ScopeStorageUtility
import com.programmergabut.scopestorageutility.manage.callback.ImageCallback
import com.programmergabut.scopestorageutility.manage.action.base.BaseAction
import com.programmergabut.scopestorageutility.util.*
import com.programmergabut.scopestorageutility.util.imageutil.compressBitmap
import com.programmergabut.scopestorageutility.util.imageutil.deleteExistingSharedFile
import com.programmergabut.scopestorageutility.util.imageutil.deleteFileIfExist
import com.programmergabut.scopestorageutility.util.imageutil.getOrCreateDirectoryIfEmpty
import com.programmergabut.scopestorageutility.util.imageutil.getOutStreamOnPrivateStorage
import com.programmergabut.scopestorageutility.util.imageutil.getOutStreamOnShareStorage
import com.programmergabut.scopestorageutility.util.imageutil.isUsingScopeStorage
import com.programmergabut.scopestorageutility.util.imageutil.validateImageQuality
import com.programmergabut.scopestorageutility.util.imageutil.validateWritePermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class Save(
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

    private fun savePrivate(quality: Int, bitmap: Bitmap) {
        validateImageQuality(quality)
        validateWritePermission(context)
        val directory = getOrCreateDirectoryIfEmpty(directory)
        val file = File(directory, "$fileName${fileExtension.extension}")
        deleteFileIfExist(file)
        runCatching {
            val outputStream = FileOutputStream(file)
            compressBitmap(outputStream, bitmap, quality, fileExtension)
        }
    }

    private fun saveShared(quality: Int, bitmap: Bitmap) {
        validateImageQuality(quality)
        validateWritePermission(context)
        deleteExistingSharedFile(context, collection, projection, cleanDirectory, where)
        val outputStream = if (isUsingScopeStorage) {
            getOutStreamOnShareStorage(
                context,
                externalStorageDirectory,
                fileName,
                fileExtension,
                env
            )
        } else {
            getOutStreamOnPrivateStorage(
                context,
                externalStorageDirectory,
                fileName,
                fileExtension,
                env
            )
        }
        compressBitmap(outputStream, bitmap, quality, fileExtension)
    }

    fun save(bitmap: Bitmap, quality: Int): Boolean {
        return try {
            if(toSharedStorage){
                saveShared(quality, bitmap)
            } else {
                savePrivate(quality, bitmap)
            }
            true
        } catch (ex: Exception){
            Log.e(ScopeStorageUtility.TAG, "save: ${ex.message}")
            false
        }
    }

    fun save(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(toSharedStorage){
                    saveShared(quality, bitmap)
                } else {
                    savePrivate(quality, bitmap)
                }
                imageCallBack.onSuccess()
            } catch (ex: Exception){
                Log.e(ScopeStorageUtility.TAG, "save: ${ex.message}")
                imageCallBack.onFailed(ex)
            }
        }
    }
}