package com.programmergabut.scopestorageutility.util

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

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