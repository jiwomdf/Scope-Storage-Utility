package com.programmergabut.androidimageutil.manage

import android.content.Context
import android.os.Environment
import com.programmergabut.androidimageutil.util.Extension

class Manage(private val context: Context) {
    fun imageAttribute(
        fileName: String,
        directory: String?,
        fileExtension: Extension,
        env: String = Environment.DIRECTORY_PICTURES
    ) = ManageImage(
        context,
        fileName,
        directory,
        fileExtension,
        env
    )
}