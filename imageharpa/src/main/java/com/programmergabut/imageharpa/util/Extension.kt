package com.programmergabut.imageharpa.util

import android.os.Build

enum class Extension {
    PNG,
    JPEG,
    JPG,
    WEBP,
}

fun setExtension(e: Extension): String {
    return when (e) {
        Extension.PNG -> ".png"
        Extension.JPEG -> ".jpeg"
        Extension.JPG -> ".jpg"
        Extension.WEBP -> ".webp"
    }
}

fun mapMimeType(fileExtension: Extension): String {
    return when (fileExtension) {
        Extension.PNG -> "image/png"
        Extension.JPEG -> "image/jpeg"
        Extension.WEBP -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                "image/webp"
            } else {
                "image/webp"
            }
        }
        Extension.JPG -> "image/jpeg"
    }
}