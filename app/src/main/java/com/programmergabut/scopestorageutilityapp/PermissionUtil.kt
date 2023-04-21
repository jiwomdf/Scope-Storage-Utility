package com.programmergabut.scopestorageutilityapp

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionUtil(
    context: Context
): ContextWrapper(context) {

    private fun isGranted(permission: String) =
        (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)

    val arrPermissionTakePhoto = arrayOf(
        CAMERA, WRITE_EXTERNAL_STORAGE,
        READ_EXTERNAL_STORAGE
    )

    val arrPermissionTakePhotoScopeStorage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(CAMERA, READ_MEDIA_IMAGES)
    } else {
        arrayOf(CAMERA, READ_EXTERNAL_STORAGE)
    }

    fun isPermissionGranted() :Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        isGranted(CAMERA) && isGranted(READ_MEDIA_IMAGES)
    } else {
        isGranted(CAMERA) && isGranted(READ_EXTERNAL_STORAGE)
    }

    fun isPermissionGrantedScopeStorage(): Boolean =
        isGranted(CAMERA) && isGranted(WRITE_EXTERNAL_STORAGE) && isGranted(READ_EXTERNAL_STORAGE)
}