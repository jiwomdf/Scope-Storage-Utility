package com.programmergabut.androidimageutil.util

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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

fun loadBitmapFromUri(context: Context, photoUri: Uri): Bitmap {
    return if(Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, photoUri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, photoUri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }
}

fun getOrCreateDirectoryIfEmpty(directory: File): File {
    if (!directory.exists())
        directory.mkdirs()
    return directory
}

fun validateDirectory(directory: File){
    if (!directory.exists()){
        throw Exception("directory is not exists")
    }
}

fun validateBase64String(base64: String) {
    if (base64.isEmpty()) {
        throw IllegalArgumentException("Base64 encoded cannot be empty")
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
    if (!writePermissionGranted(context)) {
        throw SecurityException("Write external storage permission is not granted")
    }
}

fun validateReadPermission(context: Context) {
    if (!readPermissionGranted(context))
        throw SecurityException("Read external storage permission is not granted")
}

fun deleteFileIfExist(file: File) {
    if(file.exists()){
        file.delete()
    }
}

fun decodeByteArray(base64: String): Bitmap {
    val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        ?: throw NullPointerException("Failed decode base64 string to bitmap")
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable)
        return drawable.bitmap

    try {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    } catch (ex: Exception) {
        throw Exception(ex.message.toString())
    }
}

fun loadPublicPhotoUri(context: Context, collection: Uri, projection: Array<String>, where: String): Uri? {
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
        return photos.first()
    } ?: return null
}

fun savePublicImage(context: Context, bitmap: Bitmap, directory: String, quality: Int, fileName: String, fileExtension: Extension): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val type = mapMimeType(fileExtension)
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, type)
                put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
            }
            val uri = context.contentResolver.insert(mediaContentUri, values) ?: throw Exception("there is an error on contentResolver.insert")
            val outputStream = context.contentResolver.openOutputStream(uri) ?: throw Exception("there is an error on contentResolver.openOutputStream")
            compressBitmap(outputStream, bitmap, quality, fileExtension)
            true
    } else {
        val imagePath = Environment.getExternalStoragePublicDirectory(directory).absolutePath
        val file = File(imagePath, fileName)
        val outputStream = FileOutputStream(file)
        compressBitmap(outputStream, bitmap, quality, fileExtension)
        true
    }
}

fun deletePublicImage(context: Context, photoUri: Uri, intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    try {
        context.contentResolver.delete(photoUri, null, null)
    } catch (e: SecurityException) {
        val intentSender = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                MediaStore.createDeleteRequest(context.contentResolver, listOf(photoUri)).intentSender
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
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

fun deleteExistingPublicImage(context: Context, collection: Uri, projection: Array<String>, where: String){
    try {
        val photoUri = loadPublicPhotoUri(context, collection, projection, where) ?: return
        context.contentResolver.delete(photoUri, null, null)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}