package com.programmergabut.scopestorageutility.manage.action.base

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.MediaQueryHelper
import java.io.File

abstract class BaseAction(
    protected val context: Context,
    protected val fileName: String,
    directory: String?,
    protected val fileExtension: Extension.ExtensionModel,
    protected val env: String
) {

    /**
     * Variable for internal image
     */
    protected val cleanDirectory = if(directory.isNullOrEmpty()) "" else directory.trim()
    private val externalFileDir = context.getExternalFilesDir(env)?.absolutePath ?: ""
    protected val directory = File("${externalFileDir}${File.separator}$cleanDirectory")

    /**
     * Variable for shared image scope storage
     */
    private val mediaQueryHelper = MediaQueryHelper(env)
    protected val collection: Uri? = mediaQueryHelper.setMediaStore()
    protected val projection = mediaQueryHelper.setMediaStoreProjection()
    protected val where = mediaQueryHelper.setMediaStoreWhere(fileName, fileExtension)

    /**
     * Variable for shared image non scope storage
     */
    protected val externalStorageDirectory = "${env}${File.separator}$cleanDirectory"
    protected val externalStorageSharedDir: String = Environment.getExternalStoragePublicDirectory(externalStorageDirectory).absolutePath
}