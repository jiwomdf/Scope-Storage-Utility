package com.programmergabut.imageharpa.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import java.io.File

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}

fun writePermissionGranted(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(
            context.applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PermissionChecker.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

fun readPermissionGranted(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(
            context.applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) ==
        PermissionChecker.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}