package com.programmergabut.scopestorageutility.manage.action

import android.content.Context
import android.util.Log
import com.programmergabut.scopestorageutility.ScopeStorageUtility
import com.programmergabut.scopestorageutility.manage.action.base.BaseAction
import com.programmergabut.scopestorageutility.manage.callback.OutputStreamCallback
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.imageutil.deleteExistingSharedFile
import com.programmergabut.scopestorageutility.util.imageutil.deleteFileIfExist
import com.programmergabut.scopestorageutility.util.imageutil.deletePrivateFile
import com.programmergabut.scopestorageutility.util.imageutil.getOrCreateDirectoryIfEmpty
import com.programmergabut.scopestorageutility.util.imageutil.getOutStreamOnShareStorage
import com.programmergabut.scopestorageutility.util.imageutil.isUsingScopeStorage
import com.programmergabut.scopestorageutility.util.imageutil.validateWritePermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class OutStream(
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

    private fun getOutputStreamPrivate(): OutputStream {
        validateWritePermission(context)
        val directory = getOrCreateDirectoryIfEmpty(directory)
        val file = File(directory, "$fileName${fileExtension.extension}")
        deleteFileIfExist(file)
        return FileOutputStream(file)
    }

    private fun getOutputStreamShared(): OutputStream {
        validateWritePermission(context)
        if (isUsingScopeStorage) {
            deleteExistingSharedFile(context, collection, projection, cleanDirectory, where)
        } else {
            deletePrivateFile(fileName, externalStorageSharedDir, fileExtension)
        }
        return getOutStreamOnShareStorage(
            context,
            externalStorageDirectory,
            fileName,
            fileExtension,
            env
        )
    }

    fun getOutStream(): OutputStream? {
        return try {
            if(toSharedStorage){
                getOutputStreamShared()
            } else {
                getOutputStreamPrivate()
            }
        } catch (ex: Exception){
            Log.e(ScopeStorageUtility.TAG, "outStream: ${ex.message}")
            null
        }
    }

    fun getOutputStream(outputStreamCallback: OutputStreamCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outStream = if(toSharedStorage){
                    getOutputStreamShared()
                } else {
                    getOutputStreamPrivate()
                }
                withContext(Dispatchers.Main) { outputStreamCallback.onSuccess(outStream) }
            } catch (ex: Exception){
                Log.e(ScopeStorageUtility.TAG, "outStream: ${ex.message}")
                withContext(Dispatchers.Main) { outputStreamCallback.onFailed(ex) }
            }
        }
    }

}