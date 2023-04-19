package com.programmergabut.androidimageutil.manage.action.base

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.programmergabut.androidimageutil.util.Extension
import com.programmergabut.androidimageutil.util.MediaQueryHelper
import java.io.File

abstract class BaseAction(
    protected val context: Context,
    protected val fileName: String,
    directory: String?,
    protected val fileExtension: Extension,
    protected val env: String
) {

    /**
     * Variable for internal image
     */
    protected val cleanDirectory = if(directory.isNullOrEmpty()) "" else directory.trim()
    private val externalFileDir = context.getExternalFilesDir(env)?.absolutePath ?: ""
    protected val directory = File("${externalFileDir}${File.separator}$cleanDirectory")

    /**
     * Variable for public image scope storage
     */
    private val mediaQueryHelper = MediaQueryHelper(env)
    protected val collection: Uri? = mediaQueryHelper.setMediaStore()
    protected val projection = mediaQueryHelper.setMediaStoreProjection()
    protected val where = mediaQueryHelper.setMediaStoreWhere(fileName, fileExtension)

    /**
     * Variable for public image non scope storage
     */
    protected val externalStorageDirectory = "${env}${File.separator}$cleanDirectory"
    protected val externalStoragePublicDir: String = Environment.getExternalStoragePublicDirectory(externalStorageDirectory).absolutePath
}