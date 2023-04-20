package com.programmergabut.androidimageutil.manage.action

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.callback.ImageCallback
import com.programmergabut.androidimageutil.manage.action.base.BaseAction
import com.programmergabut.androidimageutil.util.*
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
    private fun deletePublicFileScopeStorage(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>): Boolean {
        return collection?.let {
            val uri = loadUriScopeStorage(context, collection, projection, where, cleanDirectory, env) ?: throw Exception("cant get photo Uri")
            deletePublicImageScopeStorageWithSecurity(context, uri, intentSenderRequest)
            true
        } ?: kotlin.run {
            Log.e(AndroidImageUtil.TAG, "loadPublicUri: collection is null")
            false
        }
    }

    private fun deletePublicFileNonSharedStorage(): Boolean {
        validateDirectory(File(externalStoragePublicDir))
        return deletePrivateFile(fileName, externalStoragePublicDir, fileExtension)
    }

    private fun deletePrivateStorage(): Boolean {
        validateDirectory(directory)
        val file = File(directory, "$fileName${fileExtension.extension}")
        return file.delete()
    }

    @SuppressLint("NewApi")
    fun delete(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?): Boolean? {
        try {
            validateFileName(fileName)
            validateReadPermission(context)
            return if(isSharedStorage){
                if(isUsingScopeStorage) {
                    validateIntentSenderRequest(intentSenderRequest)
                    deletePublicFileScopeStorage(intentSenderRequest!!)
                } else {
                    deletePublicFileNonSharedStorage()
                }
            } else {
                deletePrivateStorage()
            }
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "deletePublic: ${ex.message}")
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
                        val isDeleted = deletePublicFileScopeStorage(intentSenderRequest!!)
                        if(isDeleted){
                            withContext(Dispatchers.Main) { callBack.onSuccess() }
                        } else{
                            withContext(Dispatchers.Main){ callBack.onFailed(Exception(("can't delete photo"))) }
                        }
                    } else {
                        val isDeleted = deletePublicFileNonSharedStorage()
                        if(isDeleted){
                            withContext(Dispatchers.Main) { callBack.onSuccess() }
                        } else{
                            withContext(Dispatchers.Main){ callBack.onFailed(Exception(("can't delete photo"))) }
                        }
                    }
                } else {
                    deletePrivateStorage()
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "deletePublic: ${ex.message}")
                withContext(Dispatchers.Main) { callBack.onFailed(ex) }
            }
        }
    }

}