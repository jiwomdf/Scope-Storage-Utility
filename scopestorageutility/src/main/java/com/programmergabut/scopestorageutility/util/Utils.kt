package com.programmergabut.scopestorageutility.util

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


private fun isGranted(context: Context, permission: String) =
    (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)

fun isReadStorageGranted(context: Context) :Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    isGranted(context, READ_MEDIA_IMAGES)
} else {
    isGranted(context, READ_EXTERNAL_STORAGE)
}

fun isWriteStorageGranted(context: Context): Boolean =
    isGranted(context, WRITE_EXTERNAL_STORAGE)