package com.programmergabut.scopestorageutility.util.imageutil

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.FileProvider
import com.programmergabut.scopestorageutility.ScopeStorageUtility
import com.programmergabut.scopestorageutility.util.ErrorMessage
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.isReadStorageGranted
import com.programmergabut.scopestorageutility.util.isWriteStorageGranted
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
val isUsingScopeStorage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun compressBitmap(outStream: OutputStream, bitmap: Bitmap, quality: Int, fileExtension: Extension.ExtensionModel) {
    when (fileExtension.extension) {
        Extension.PNG -> {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outStream)
        }
        Extension.JPEG -> {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
        }
        Extension.WEBP -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, quality, outStream)
            } else {
                bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outStream)
            }
        }
        Extension.JPG -> {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
        }
    }

    outStream.flush()
    outStream.close()
}

fun getOrCreateDirectoryIfEmpty(directory: File): File {
    if (!directory.exists())
        directory.mkdirs()
    return directory
}

fun validateDirectory(directory: File){
    if (!directory.exists()){
        throw Exception(ErrorMessage.DIRECTORY_NOT_FOUND)
    }
}

fun validateImageQuality(quality: Int) {
    if (quality < 1 || quality > 100) {
        throw IllegalArgumentException(ErrorMessage.QUALITY_MUST_BE_BETWEEN_1_TO_100)
    }
}

fun validateFileName(fileName: String): String {
    val fixFileName = fileName.trim()
    if (fixFileName.isEmpty())
        throw IllegalArgumentException(ErrorMessage.FILE_NAME_CANNOT_BE_EMPTY)

    return fixFileName
}

fun validateWritePermission(context: Context) {
    if(!isUsingScopeStorage){
        if (!isWriteStorageGranted(context)) {
            throw SecurityException(ErrorMessage.WRITE_EXTERNAL_STORAGE_PERMISSION_IS_NOT_GRANTED)
        }
    }
}

fun validateReadPermission(context: Context) {
    if (!isReadStorageGranted(context)){
        throw SecurityException(ErrorMessage.READ_EXTERNAL_STORAGE_PERMISSION_IS_NOT_GRANTED)
    }
}

fun validateIntentSenderRequest(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?) {
    if(isUsingScopeStorage){
        if (intentSenderRequest == null){
            throw SecurityException(ErrorMessage.INTENT_SENDER_REQUEST_IS_REQUIRE_IN_SHARED_DIRECTORY_DELETE)
        }
    }
}

fun deleteFileIfExist(file: File) {
    if(file.exists()){
        file.delete()
    }
}

fun deletePrivateFile(fileName: String, filePath: String, fileExtension: Extension.ExtensionModel): Boolean {
    val files = File("$filePath${File.separator}$fileName${fileExtension.extension}")
    return files.delete()
}

fun loadUriPrivateStorage(
    appId: String,
    activity: Activity,
    fileName: String,
    filePath: String,
    fileExtension: Extension.ExtensionModel
): Uri? {
    val file = File("$filePath${File.separator}$fileName${fileExtension.extension}")
    return file.let {
        FileProvider.getUriForFile(activity, "$appId.provider", it)
    } ?: run {
        Log.e(ScopeStorageUtility.TAG, "loadSharedUri: File not found")
        null
    }
}

fun getOutStreamOnPrivateStorage(
    context: Context,
    directory: String,
    fileName: String,
    fileExtension: Extension.ExtensionModel,
    env: String
): FileOutputStream {
    val filePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
    val fileDir = File(filePath)
    fileDir.mkdirs()
    val file = File(filePath, "$fileName${fileExtension.extension}")
    file.createNewFile()
    return FileOutputStream(file)
}
