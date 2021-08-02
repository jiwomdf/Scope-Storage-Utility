package com.programmergabut.easyimage.manage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.setExtension
import java.io.*

class SaveImage(
    private val context: Context,
    private val fileName: String,
    private val directory: String,
    private val fileExtension: Extension
) {

    fun save(base64: String,quality: Int = 100){

        validateImageQuality(quality)
        validateBase64String(base64)
        validateStoragePermission()
        val bitmap = decodeByteArray(base64)
        val extension = setExtension(fileExtension)

        val directory = File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
        if(!directory.exists())
            directory.mkdirs()

        val file = File(directory,"$fileName$extension")

        try{
            compressBitmap(file, bitmap, quality)
        }catch (e: FileNotFoundException){
            e.printStackTrace()
        }
        catch (e : IOException){
            e.printStackTrace()
        }

    }

    private fun compressBitmap(file: File, bitmap: Bitmap, quality: Int) {
        val outStream = FileOutputStream(file)

        when (fileExtension) {
            Extension.PNG -> {
                bitmap.compress(Bitmap.CompressFormat.PNG,quality,outStream)
            }
            Extension.JPEG -> {
                bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outStream)
            }
            Extension.WEBP -> {
                bitmap.compress(Bitmap.CompressFormat.WEBP,quality,outStream)
            }
            Extension.JPG -> {
                bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outStream)
            }
        }

        outStream.flush()
        outStream.close()
    }

    private fun validateStoragePermission() {
        if(!writePermissionGranted()){
            throw SecurityException("Write External Storage Permission Not Granted")
        }
    }

    private fun decodeByteArray(base64: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            ?: throw NullPointerException("Decoding Base64 String results in Null Bitmap")
    }

    private fun validateBase64String(base64: String) {
        if(base64.isEmpty()){
            throw IllegalArgumentException("Base64 encoded cannot be empty")
        }
    }

    private fun validateImageQuality(quality: Int) {
        if(quality < 1 || quality <= 100){
            throw IllegalArgumentException("Quality must be between 1 to 100")
        }
    }

    private fun writePermissionGranted(): Boolean{
        if(ContextCompat.checkSelfPermission(context.applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED){
            return true
        }
        return false
    }

}