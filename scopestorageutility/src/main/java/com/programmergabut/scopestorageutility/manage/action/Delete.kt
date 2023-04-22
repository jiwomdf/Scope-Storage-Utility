package com.programmergabut.scopestorageutility.manage.action

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.programmergabut.scopestorageutility.ScopeStorageUtility
import com.programmergabut.scopestorageutility.manage.callback.ImageCallback
import com.programmergabut.scopestorageutility.manage.action.base.BaseAction
import com.programmergabut.scopestorageutility.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Delete(
    context: Context,
    fileName: String,
    directory: String?,
    fileExtension: Extension.ExtensionModel,
    env: String,
    private val isSharedStorage: Boolean
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    @SuppressLint("NewApi")
    private fun deleteSharedFileScopeStorage(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>): Boolean {
        return collection?.let {
            val uri = loadUriScopeStorage(context, collection, projection, where, cleanDirectory, env) ?: throw Exception(ErrorMessage.CANT_GET_PHOTO_URI)
            val isDeleted = deleteSharedImageScopeStorageWithSecurity(context, uri, intentSenderRequest)
            isDeleted
        } ?: kotlin.run {
            Log.e(ScopeStorageUtility.TAG, "deleteSharedFileScopeStorage: collection is null")
            false
        }
    }

    private fun deleteSharedFileNonSharedStorage(): Boolean {
        validateDirectory(File(externalStorageSharedDir))
        return deletePrivateFile(fileName, externalStorageSharedDir, fileExtension)
    }

    private fun deletePrivateStorage(): Boolean {
        validateDirectory(directory)
        val file = File(directory, "$fileName${fileExtension.extension}")
        return file.delete()
    }

    @SuppressLint("NewApi")
    fun delete(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?): Boolean {
        try {
            validateFileName(fileName)
            validateReadPermission(context)
            return if(isSharedStorage){
                if(isUsingScopeStorage) {
                    validateIntentSenderRequest(intentSenderRequest)
                    deleteSharedFileScopeStorage(intentSenderRequest!!)
                } else {
                    deleteSharedFileNonSharedStorage()
                }
            } else {
                deletePrivateStorage()
            }
        } catch (ex: Exception){
            Log.e(ScopeStorageUtility.TAG, "deleteShared: ${ex.message}")
            return false
        }
    }

    @SuppressLint("NewApi")
    fun delete(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?, callBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateFileName(fileName)
                validateReadPermission(context)
                if(isSharedStorage){
                    if(isUsingScopeStorage){
                        validateIntentSenderRequest(intentSenderRequest)
                        val isDeleted = deleteSharedFileScopeStorage(intentSenderRequest!!)
                        if(isDeleted){
                            withContext(Dispatchers.Main) { callBack.onSuccess() }
                        } else{
                            withContext(Dispatchers.Main){ callBack.onFailed(Exception((ErrorMessage.FAILED_DELETE_PHOTO))) }
                        }
                    } else {
                        val isDeleted = deleteSharedFileNonSharedStorage()
                        if(isDeleted){
                            withContext(Dispatchers.Main) { callBack.onSuccess() }
                        } else{
                            withContext(Dispatchers.Main){ callBack.onFailed(Exception((ErrorMessage.FAILED_DELETE_PHOTO))) }
                        }
                    }
                } else {
                    deletePrivateStorage()
                }
            } catch (ex: Exception){
                Log.e(ScopeStorageUtility.TAG, "deleteShared: ${ex.message}")
                withContext(Dispatchers.Main) { callBack.onFailed(ex) }
            }
        }
    }

}