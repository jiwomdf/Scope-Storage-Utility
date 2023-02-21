package com.programmergabut.androidimageutil.manage

import android.content.Context
import android.os.Environment
import com.programmergabut.androidimageutil.util.Extension

class Manage(private val context: Context) {
    fun imageAttribute(
        fileName: String,
        directory: String?,
        env: String = Environment.DIRECTORY_DCIM,
        fileExtension: Extension,
        isSharedStorage: Boolean,
    ) = ManageImage(
        context = context,
        fileName = fileName,
        directory = directory,
        env = env,
        fileExtension = fileExtension,
        isSharedStorage = isSharedStorage,
    )
}