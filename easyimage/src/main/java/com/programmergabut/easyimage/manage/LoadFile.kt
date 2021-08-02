package com.programmergabut.easyimage.manage

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.programmergabut.easyimage.Extension
import java.io.File

class LoadFile(private val context: Context) {


    private var fileExtension: String? = null
    private var fileName: String? = null
    private var subDirectory: String? = null
    private var directory: String? = context.applicationContext.packageName

    fun directory(dir: String): LoadFile {

        val fixDir = dir.trim()

        if (fixDir.isNotEmpty()) {
            directory = fixDir
        } else {
            throw IllegalArgumentException("Directory cannot be empty")
        }

        return this
    }

    fun subDirectory(subDir: String?): LoadFile {

        val fixSubDir = subDir?.trim()
        if (!fixSubDir.isNullOrEmpty()) {
            subDirectory = if (subDirectory != null) {
                "$subDirectory/$fixSubDir"
            } else {
                subDir
            }
        } else {
            throw java.lang.IllegalArgumentException("Sub directory cannot be empty or null")
        }
        return this
    }

    fun withExtension(e: Extension): LoadFile {

        fileExtension = when(e){
            Extension.PNG -> ".png"
            Extension.JPEG -> ".jpeg"
            Extension.JPG -> ".jpg"
            Extension.WEBP -> ".webp"
        }

        return this
    }

    fun setFileName(name :String?): LoadFile {

        val trimmedName = name?.trim()

        if(!trimmedName.isNullOrEmpty()){
            val fixName = trimmedName.replace(" ","_")
            fileName = fixName
        }else{
            throw java.lang.IllegalArgumentException("File Name cannot be Empty or Null")
        }
        return this
    }

    fun load(): Bitmap?{

        if(fileExtension == null)
            throw NullPointerException("File extension null. Use withExtension() Method")

        if(fileName == null)
            throw NullPointerException("File Name Null. Use setFileName() Method")

        if(!readPermissionGranted())
            throw SecurityException("Write External Storage Permission Not Granted")

        val dir = if(subDirectory == null)
            File("${context.getExternalFilesDir(null)?.absolutePath}/$directory")
        else
            File("${context.getExternalFilesDir(null)?.absolutePath}/$directory/$subDirectory")

        //Create Directory If Doesn't Exist
        if(!dir.exists()){
            return null
        }

        val file = File(dir,"$fileName$fileExtension")

        return BitmapFactory.decodeFile(file.path)
    }

    private fun readPermissionGranted(): Boolean{
        if(ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PermissionChecker.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

}