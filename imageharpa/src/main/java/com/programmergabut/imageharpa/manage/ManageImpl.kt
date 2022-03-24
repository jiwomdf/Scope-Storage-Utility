package com.programmergabut.imageharpa.manage

import android.content.Context
import com.programmergabut.imageharpa.domain.Manage
import com.programmergabut.imageharpa.domain.ManageImage
import com.programmergabut.imageharpa.util.Extension

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