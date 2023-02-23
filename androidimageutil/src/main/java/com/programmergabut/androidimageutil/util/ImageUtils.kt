package com.programmergabut.androidimageutil.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.programmergabut.androidimageutil.AndroidImageUtil
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

val isUsingScopeStorage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun compressBitmap(outStream: OutputStream, bitmap: Bitmap, quality: Int, fileExtension: Extension) {
    when (fileExtension) {
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
        throw IllegalArgumentException("Quality must be between 1 to 100")
    }
}

fun validateFileName(fileName: String): String {
    val fixFileName = fileName.trim()
    if (fixFileName.isEmpty())
        throw IllegalArgumentException("File name cannot be empty")

    return fixFileName
}

fun validateStoragePermission(context: Context) {
    if(!isUsingScopeStorage){
        if (!writePermissionGranted(context)) {
            throw SecurityException("Write external storage permission is not granted")
        }
    }
}

fun validateReadPermission(context: Context) {
    if(!isUsingScopeStorage){
        if (!readPermissionGranted(context)){
            throw SecurityException("Read external storage permission is not granted")
        }
    }
}

fun validateIntentSenderRequest(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>?) {
    if(isUsingScopeStorage){
        if (intentSenderRequest == null){
            throw SecurityException("intentSenderRequest is require in public directory delete")
        }
    }
}

fun deleteFileIfExist(file: File) {
    if(file.exists()){
        file.delete()
    }
}

fun loadBitmapFromUri(context: Context, photoUri: Uri): Bitmap {
    return if(Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, photoUri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, photoUri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}

fun loadPublicPhotoUri(context: Context, collection: Uri?, projection: Array<String>, where: String): Uri? {
    try {
        if(collection == null) return null
        val photos = mutableListOf<Uri>()
        context.contentResolver.query(
            collection,
            projection,
            where,
            null,
            "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                photos.add(contentUri)
            }
            return photos.firstOrNull()
        } ?: return null
    } catch (ex: Exception) {
        return null
    }
}

@SuppressLint("NewApi")
fun deletePublicImageScopeStorageWithSecurity(context: Context, photoUri: Uri, intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    try {
        context.contentResolver.delete(photoUri, null, null)
    } catch (e: SecurityException) {
        val intentSender = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                MediaStore.createDeleteRequest(context.contentResolver, listOf(photoUri)).intentSender
            }
            isUsingScopeStorage -> {
                val recoverableSecurityException = e as? RecoverableSecurityException
                recoverableSecurityException?.userAction?.actionIntent?.intentSender
            }
            else -> null
        }
        intentSender?.let { sender ->
            intentSenderLauncher.launch(
                IntentSenderRequest.Builder(sender).build()
            )
        }
    }
}

fun deletePrivateImage(fileName: String, filePath: String): Boolean? {
    val files = File(filePath)
    var existsFile: File? = null
    files.walk().forEach ret@{
        if(it.name.equals(fileName, true)){
            existsFile = it
            return@ret
        }
    }
    return existsFile?.delete()
}

fun deleteExistingPublicImage(context: Context, collection: Uri?, projection: Array<String>, where: String){
    try {
        if(collection == null) return
        val photoUri = loadPublicPhotoUri(context, collection, projection, where) ?: return
        context.contentResolver.delete(photoUri, null, null)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}

fun getOutStream(
    context: Context,
    directory: String,
    fileName: String,
    fileExtension: Extension,
    env: String
): OutputStream {
    return if (isUsingScopeStorage) {
        val mediaContentUri: Uri = MediaQueryHelper(env).setMediaStore() ?: throw Exception("there is an error on get query media type")

        val type = mapMimeType(fileExtension)
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, type)
            put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
        }
        val uri = context.contentResolver.insert(mediaContentUri, values) ?: throw Exception("there is an error on contentResolver.insert")
        val outputStream = context.contentResolver.openOutputStream(uri) ?: throw Exception("there is an error on contentResolver.openOutputStream")
        outputStream
    } else {
        val filePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
        val fileDir = File(filePath)
        fileDir.mkdirs()
        val fileExt = setExtension(fileExtension)
        val file = File(filePath, "$fileName$fileExt")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        outputStream
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun loadUriScopeStorage(context: Context, collection: Uri, projection: Array<String>, where: String, env: String): Uri? {
    try {
        val photos = mutableListOf<Uri>()
        val mediaQueryHelper = MediaQueryHelper(env)
        context.contentResolver.query(
            collection,
            projection,
            where,
            null,
            "${mediaQueryHelper.getMediaStoreDisplayName()} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(mediaQueryHelper.getMediaStoreId())
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                mediaQueryHelper.setMediaStore()?.let {
                    val contentUri = ContentUris.withAppendedId(it, id)
                    photos.add(contentUri)
                }
            }
            return photos.firstOrNull()
        } ?: return null
    } catch (ex: Exception) {
        return null
    }
}

fun loadUriPrivateStorage(appId: String, activity: Activity, files: File, fileName: String, filePath: String, fileExtension: Extension): Uri? {
    var existsFile: File? = null
    files.walk().forEach ret@{
        if(it.name.contains(fileName, true)){
            existsFile = it
            return@ret
        }
    }

    return existsFile?.let {
        FileProvider.getUriForFile(activity, "$appId.provider", it)
    } ?: run {
        Log.e(AndroidImageUtil.TAG, "loadPublicUri: File not found")
        null
    }
}
