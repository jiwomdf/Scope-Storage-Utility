package com.programmergabut.androidimageutil.manage.action.base

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.programmergabut.androidimageutil.util.Extension
import com.programmergabut.androidimageutil.util.sdk29AndUp
import com.programmergabut.androidimageutil.util.setExtension

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
    protected val absolutePath = context.getExternalFilesDir(env)?.absolutePath ?: ""
    protected val finalDirectory = if(directory.isNullOrEmpty()) "" else directory.trim()

    /**
     * Variable for public image
     */
    protected val collection: Uri = sdk29AndUp { MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) }
        ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    protected val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,)
    protected val where = MediaStore.Images.Media.DISPLAY_NAME + " LIKE " + "'$fileName${setExtension(fileExtension)}'"
}