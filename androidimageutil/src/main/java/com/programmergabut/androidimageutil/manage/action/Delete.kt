package com.programmergabut.androidimageutil.manage.action

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.programmergabut.androidimageutil.AndroidImageUtil
import com.programmergabut.androidimageutil.manage.ImageCallback
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
    env: String
): BaseAction(
    context,
    fileName,
    directory,
    fileExtension,
    env
) {

    fun d(): Boolean {
        return try {
            val extension = setExtension(fileExtension)
            val directory = File("${absolutePath}${File.separator}$finalDirectory")
            validateDirectory(directory)

            val file = File(directory, "$fileName$extension")
            file.delete()
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "delete: ${ex.message}")
            true
        }
    }

    fun d(callBack: ImageCallback){
        CoroutineScope(Dispatchers.IO).launch {
            val extension = setExtension(fileExtension)
            val directory = File("${absolutePath}${File.separator}$finalDirectory")
            validateDirectory(directory)

            try {
                val file = File(directory, "$fileName$extension")
                if(file.delete()){
                    withContext(Dispatchers.Main) { callBack.onSuccess() }
                } else {
                    throw Exception("file not deleted")
                }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "delete: ${ex.message}")
                withContext(Dispatchers.Main) {
                    callBack.onFailed(ex)
                }
            }
        }
    }

    fun dPublic(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>): Boolean {
        return try {
            validateFileName(fileName)
            validateReadPermission(context)
            val uri = loadPublicPhotoUri(context, collection, projection, where) ?: throw Exception("cant get photo Uri")
            deletePublicImage(context, uri, intentSenderRequest)
            true
        } catch (ex: Exception){
            Log.e(AndroidImageUtil.TAG, "deletePublic: ${ex.message}", )
            false
        }
    }

    fun dPublic(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>, callBack: ImageCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validateFileName(fileName)
                validateReadPermission(context)
                val uri = loadPublicPhotoUri(context, collection, projection, where) ?: throw Exception("cant get photo Uri")
                deletePublicImage(context, uri, intentSenderRequest)
                withContext(Dispatchers.Main) { callBack.onSuccess() }
            } catch (ex: Exception){
                Log.e(AndroidImageUtil.TAG, "deletePublic: ${ex.message}")
                withContext(Dispatchers.Main) {
                    callBack.onFailed(ex)
                }
            }
        }
    }

}