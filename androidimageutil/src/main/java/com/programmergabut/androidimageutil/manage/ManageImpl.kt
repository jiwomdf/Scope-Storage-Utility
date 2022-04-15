package com.programmergabut.androidimageutil.manage

import android.content.Context
import com.programmergabut.androidimageutil.domain.Manage
import com.programmergabut.androidimageutil.domain.ManageImage
import com.programmergabut.androidimageutil.util.Extension

class ManageImpl(private val context: Context): Manage {
    override fun imageAttribute(
        fileName: String,
        directory: String?,
        fileExtension: Extension
    ) = ManageImageImpl(
        context,
        fileName,
        directory,
        fileExtension
    ) as ManageImage
}