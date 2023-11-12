package com.programmergabut.scopestorageutility.model

import android.os.Environment
import com.programmergabut.scopestorageutility.util.Extension

data class UtilityModel(
    val fileName: String,
    val directory: String?,
    val env: String = Environment.DIRECTORY_DCIM,
    val extension: Extension.ExtensionModel
)