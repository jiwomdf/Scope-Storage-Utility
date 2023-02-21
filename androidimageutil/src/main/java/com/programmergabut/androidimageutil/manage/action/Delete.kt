package com.programmergabut.androidimageutil.manage.action

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
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
    fileExtension: Extension,
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
    private fun deletePublicFileShareStorage(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>): Boolean {
        return collectionFile?.let {
            val uri = loadUri(context, collectionFile, projectionFile, whereFile) ?: throw Exception("cant get photo Uri")
            deletePublicImage(context, uri, intentSenderRequest)
            true
        } ?: kotlin.run {
            Log.e(AndroidImageUtil.TAG, "loadPublicUri: collection is null")
            false
        }
    }

    private fun deletePublicFileNonSharedStorage(): Boolean? {
        val filePath = Environment.getExternalStoragePublicDirectory("${env}${File.separator}$finalDirectory").absolutePath
        validateDirectory(File(filePath))
        return deletePrivateImage(fileName, filePath, fileExtension)
    }

    private fun deletePrivateStorage(): Boolean {
        val extension = setExtension(fileExtension)
        val directory = File("${absolutePath}${File.separator}$finalDirectory")
        validateDirectory(directory)
        val file = File(directory, "$fileName$extension")
        return file.delete()
    }

    @SuppressLint("NewApi")
    fun deletePrivateStorage(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?): Boolean? {
        try {
            validateFileName(fileName)
            validateReadPermission(context)
            return if(isSharedStorage){
                if(isUsingScopeStorage) {
                    validateIntentSenderRequest(intentSenderRequest)
                    deletePublicFileShareStorage(intentSenderRequest!!)
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
    fun deletePrivateStorage(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?, callBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateFileName(fileName)
                validateReadPermission(context)
                if(isSharedStorage){
                    if(isUsingScopeStorage){
                        validateIntentSenderRequest(intentSenderRequest)
                        val isDeleted = deletePublicFileShareStorage(intentSenderRequest!!)
                        if(isDeleted){
                            withContext(Dispatchers.Main) { callBack.onSuccess() }
                        } else{
                            withContext(Dispatchers.Main){ callBack.onFailed(Exception(("can't delete photo"))) }
                        }
                    } else {
                        val isDeleted = deletePublicFileNonSharedStorage()
                        if(isDeleted == true){
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