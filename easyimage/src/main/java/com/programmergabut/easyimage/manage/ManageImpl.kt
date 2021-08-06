package com.programmergabut.easyimage.manage

import android.content.Context
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.domain.Manage
import com.programmergabut.easyimage.domain.ManageImage

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