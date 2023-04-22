package com.programmergabut.scopestorageutility.manage

import android.content.Context
import android.os.Environment
import com.programmergabut.scopestorageutility.util.Extension

class Manage(private val context: Context) {

    private var isSharedStorage: Boolean = false

    fun isShareStorage(value: Boolean): Manage {
        this.isSharedStorage = value
        return this
    }

    fun attribute(
        fileName: String,
        directory: String?,
        env: String = Environment.DIRECTORY_DCIM,
        extension: Extension.ExtensionModel
    ) = ManageImage(
        context = context,
        fileName = fileName,
        directory = directory,
        env = env,
        fileExtension = extension,
        isSharedStorage = isSharedStorage,
    )
}