package com.programmergabut.androidimageutil.util

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

class MediaQueryHelper(
    private val env: String,
) {

    /**
     *  https://developer.android.com/training/data-storage/shared/media
     **/

    @SuppressLint("InlinedApi")
    fun setMediaStore(): Uri? {
        return when (env) {
            Environment.DIRECTORY_DOWNLOADS -> {
                if(isUsingScopeStorage) {
                    MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    null
                }
            }
            Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES -> {
                if(isUsingScopeStorage) {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }
            else -> {
                if(isUsingScopeStorage) {
                    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    null
                }
            }
        }
    }

    fun setMediaStoreProjection(): Array<String> {
        return when (env) {
            Environment.DIRECTORY_DOWNLOADS -> {
                arrayOf(MediaStore.Downloads._ID, MediaStore.Downloads.DISPLAY_NAME)
            }
            Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES -> {
                arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
            }
            else -> {
                arrayOf(MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DISPLAY_NAME)
            }
        }
    }

    fun setMediaStoreWhere(fileName: String, fileExtension: Extension.ExtensionModel): String {
        return when (env) {
            Environment.DIRECTORY_DOWNLOADS -> {
                MediaStore.Downloads.DISPLAY_NAME + " LIKE " + "'$fileName${fileExtension.extension}' AND " +
                        MediaStore.Downloads.DATA + " LIKE ? "
            }
            Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES -> {
                MediaStore.Images.Media.DISPLAY_NAME + " LIKE " + "'$fileName${fileExtension.extension}' AND " +
                        MediaStore.Images.Media.DATA + " LIKE ? "
            }
            else -> {
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE " + "'$fileName${fileExtension.extension}' AND " +
                        MediaStore.Files.FileColumns.DATA + " LIKE ? "
            }
        }
    }

    fun getMediaStoreId(): String {
        return when (env) {
            Environment.DIRECTORY_DOWNLOADS -> {
                MediaStore.Downloads._ID
            }
            Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES -> {
                MediaStore.Images.Media._ID
            }
            else -> {
                MediaStore.Files.FileColumns._ID
            }
        }
    }

    fun getMediaStoreDisplayName(): String {
        return when (env) {
            Environment.DIRECTORY_DOWNLOADS -> {
                MediaStore.Downloads.DISPLAY_NAME
            }
            Environment.DIRECTORY_DCIM, Environment.DIRECTORY_PICTURES -> {
                MediaStore.Images.Media.DISPLAY_NAME
            }
            else -> {
                MediaStore.Files.FileColumns.DISPLAY_NAME
            }
        }
    }

}