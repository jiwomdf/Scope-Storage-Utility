package com.programmergabut.easyimage

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