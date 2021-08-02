package com.programmergabut.easyimage.manage

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.setExtension
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException

class LoadImage(
    private val context: Context,
    private val fileName: String,
    private val directory: String,
    private val fileExtension: Extension
) {

    fun load(): Bitmap? {

        validateFileName(fileName)
        validateDirectoryName(directory)
        validateReadPermission()
        val extension = setExtension(fileExtension)

        val directory = File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
        if (!directory.exists())
            return null

        val file = File(directory, "$fileName$extension")

        return BitmapFactory.decodeFile(file.path)
    }

    fun delete(): Boolean {
        val extension = setExtension(fileExtension)
        val directory = File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
        if (!directory.exists())
            return false

        val file = File(directory, "$fileName$extension")

        return file.delete()
    }

    fun save(bitmap: Bitmap, quality: Int = 100) {
        validateImageQuality(quality)
        validateStoragePermission()
        val extension = setExtension(fileExtension)
        val directory = getDirectory()

        try {
            val file = File(directory, "$fileName$extension")
            compressBitmap(file, bitmap, quality)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun save(base64: String, quality: Int = 100) {
        validateImageQuality(quality)
        validateBase64String(base64)
        validateStoragePermission()
        val bitmap = decodeByteArray(base64)
        val extension = setExtension(fileExtension)
        val directory = getDirectory()

        try {
            val file = File(directory, "$fileName$extension")
            compressBitmap(file, bitmap, quality)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun save(drawable: Drawable, quality: Int = 100) {
        validateImageQuality(quality)
        validateStoragePermission()
        val bitmap = drawableToBitmap(drawable)
        val extension = setExtension(fileExtension)
        val directory = getDirectory()

        try {
            val file = File(directory, "$fileName$extension")
            compressBitmap(file, bitmap, quality)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getDirectory(): File {
        val directory = File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
        if (!directory.exists())
            directory.mkdirs()

        return directory
    }

    private fun compressBitmap(file: File, bitmap: Bitmap, quality: Int) {
        val outStream = FileOutputStream(file)

        when (fileExtension) {
            Extension.PNG -> {
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, outStream)
            }
            Extension.JPEG -> {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
            }
            Extension.WEBP -> {
                bitmap.compress(Bitmap.CompressFormat.WEBP, quality, outStream)
            }
            Extension.JPG -> {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)
            }
        }

        outStream.flush()
        outStream.close()
    }

    private fun decodeByteArray(base64: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            ?: throw NullPointerException("Decoding Base64 String results in Null Bitmap")
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
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

    private fun validateBase64String(base64: String) {
        if (base64.isEmpty()) {
            throw IllegalArgumentException("Base64 encoded cannot be empty")
        }
    }

    private fun validateImageQuality(quality: Int) {
        if (quality < 1 || quality <= 100) {
            throw IllegalArgumentException("Quality must be between 1 to 100")
        }
    }

    private fun validateFileName(fileName: String): String {
        val fixFileName = fileName.trim()
        if (fixFileName.isEmpty())
            throw IllegalArgumentException("File name cannot be empty")

        return fixFileName
    }

    private fun validateDirectoryName(dirName: String): String {
        val fixDirName = dirName.trim()
        if (fixDirName.isEmpty())
            throw IllegalArgumentException("Directory name cannot be empty")

        return fixDirName
    }

    private fun validateStoragePermission() {
        if (!writePermissionGranted()) {
            throw SecurityException("Write External Storage Permission Not Granted")
        }
    }

    private fun validateReadPermission() {
        if (!readPermissionGranted())
            throw SecurityException("Write external storage permission is not been granted")
    }

    private fun writePermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun readPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) ==
            PermissionChecker.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
}
