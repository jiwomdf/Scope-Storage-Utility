package com.programmergabut.androidimageutil.util

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
        Extension.WEBP -> "image/webp"
        Extension.JPG -> "image/jpeg"
    }
}