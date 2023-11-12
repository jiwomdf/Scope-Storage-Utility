package com.programmergabut.scopestorageutility.manage

import android.content.Context
import android.os.Environment
import com.programmergabut.scopestorageutility.model.UtilityModel
import com.programmergabut.scopestorageutility.util.Extension

class Manage(private val context: Context) {

    private var isSharedStorage: Boolean = false

    fun isShareStorage(value: Boolean): Manage {
        this.isSharedStorage = value
        return this
    }

    fun attribute(utilityModel: UtilityModel) = ManageImage(
        context = context,
        fileName = utilityModel.fileName,
        directory = utilityModel.directory,
        env = utilityModel.env,
        fileExtension = utilityModel.extension,
        isSharedStorage = isSharedStorage,
    )
}