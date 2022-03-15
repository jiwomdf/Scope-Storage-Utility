package com.programmergabut.imageharpa.manage

import android.content.Context
import com.programmergabut.imageharpa.Extension
import com.programmergabut.imageharpa.domain.Manage
import com.programmergabut.imageharpa.domain.ManageImage

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