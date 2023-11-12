package com.programmergabut.scopestorageutility.util.imageutil

import android.annotation.SuppressLint
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.MediaQueryHelper
import java.io.OutputStream

fun loadSharedUri(
    context: Context,
    collection: Uri?,
    projection: Array<String>,
    directory: String,
    where: String
): Uri? {
    try {
        if(collection == null) return null
        val photos = mutableListOf<Uri>()
        context.contentResolver.query(
            collection,
            projection,
            where,
            arrayOf("%$directory%"),
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
fun deleteSharedImageScopeStorageWithSecurity(context: Context, photoUri: Uri, intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>): Boolean {
    return try {
        context.contentResolver.delete(photoUri, null, null)
        true
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
        false
    }
}

fun deleteExistingSharedFile(
    context: Context,
    collection: Uri?,
    projection: Array<String>,
    directory: String,
    where: String
){
    try {
        if(collection == null) return
        val photoUri = loadSharedUri(context, collection, projection, directory, where) ?: return
        context.contentResolver.delete(photoUri, null, null)
    } catch (e: SecurityException) {
        e.printStackTrace()
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

fun getOutStreamOnShareStorage(
    context: Context,
    directory: String,
    fileName: String,
    fileExtension: Extension.ExtensionModel,
    env: String
): OutputStream {
    val mediaContentUri: Uri = MediaQueryHelper(env).setMediaStore()
        ?: throw Exception("there is an error on get query media type")
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, fileExtension.mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
    }
    val uri = context.contentResolver.insert(mediaContentUri, values)
        ?: throw Exception("there is an error on contentResolver.insert")
    return context.contentResolver.openOutputStream(uri)
        ?: throw Exception("there is an error on contentResolver.openOutputStream")
}

@RequiresApi(Build.VERSION_CODES.Q)
fun loadUriScopeStorage(
    context: Context,
    collection: Uri,
    projection: Array<String>,
    where: String,
    directory: String,
    env: String
): Uri? {
    try {
        val file = mutableListOf<Uri>()
        val mediaQueryHelper = MediaQueryHelper(env)
        context.contentResolver.query(
            collection,
            projection,
            where,
            arrayOf("%$directory%"),
            "${mediaQueryHelper.getMediaStoreDisplayName()} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(mediaQueryHelper.getMediaStoreId())
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                mediaQueryHelper.setMediaStore()?.let {
                    val contentUri = ContentUris.withAppendedId(it, id)
                    file.add(contentUri)
                }
            }
            return file.firstOrNull()
        } ?: return null
    } catch (ex: Exception) {
        return null
    }
}