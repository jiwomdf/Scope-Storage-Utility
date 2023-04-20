package com.programmergabut.androidimageutil.util



object Extension {
    const val PNG = ".png"
    const val JPEG = ".jpeg"
    const val JPG = ".jpg"
    const val WEBP = ".webp"

    private val extensions = mutableListOf(
        ExtensionModel(
            extension = PNG,
            mimeType = "image/png"
        ),
        ExtensionModel(
            extension = JPEG,
            mimeType = "image/jpeg"
        ),
        ExtensionModel(
            extension = JPG,
            mimeType = "image/jpeg"
        ),
        ExtensionModel(
            extension = WEBP,
            mimeType = ".webp"
        ),
    )

    data class ExtensionModel(
        val extension: String,
        val mimeType: String
    )

    fun get(extension: String): ExtensionModel = extensions.find { it.extension == extension }
        ?: throw NullPointerException("Extension doesn't exists")

    fun getOrNull(extension: String): ExtensionModel? = extensions.find { it.extension == extension }
}