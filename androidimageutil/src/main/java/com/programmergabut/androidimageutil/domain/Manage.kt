package com.programmergabut.androidimageutil.domain

import com.programmergabut.androidimageutil.util.Extension


interface Manage {

    /**
     *  Set the attribute file you want to save or load
     *  @param fileName is the name of your file.
     *  @param directory is the directory of the file you want to save or load,
     *  if it is null or empty, it will saved to your absolute path.
     *  @param fileExtension is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
     */
    fun imageAttribute(fileName: String, directory: String?, fileExtension: Extension): ManageImage
}