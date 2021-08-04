package com.programmergabut.easyimage.domain

import com.programmergabut.easyimage.Extension

interface Manage {

    fun imageAttribute(fileName: String, directory: String, fileExtension: Extension): ManageImage
}