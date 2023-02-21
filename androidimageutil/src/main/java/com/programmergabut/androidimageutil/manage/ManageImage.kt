package com.programmergabut.androidimageutil.manage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import com.programmergabut.androidimageutil.manage.action.Delete
import com.programmergabut.androidimageutil.manage.action.Load
import com.programmergabut.androidimageutil.manage.action.Save
import com.programmergabut.androidimageutil.manage.callback.ImageCallback
import com.programmergabut.androidimageutil.manage.callback.LoadImageCallback
import com.programmergabut.androidimageutil.manage.callback.OutStreamCallback
import com.programmergabut.androidimageutil.util.Extension
import java.io.OutputStream

class ManageImage(
    context: Context,
    fileName: String,
    directory: String?,
    env: String = Environment.DIRECTORY_DCIM,
    fileExtension: Extension,
    isSharedStorage: Boolean,
) {
    /**
     *  Delete Region
     */
    private val deleteObj = Delete(context, fileName, directory, fileExtension, env, isSharedStorage)
    fun delete(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>) = deleteObj.deletePrivateStorage(intentSenderRequest)
    fun delete(
        intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>? = null,
        block: () -> Unit,
        catch: ((e: Exception) -> Unit)? = null,
    ) {
        deleteObj.deletePrivateStorage(intentSenderRequest, object: ImageCallback {
            override fun onSuccess() { block.invoke() }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }

    /**
     *  Load Region
     */
    private val loadObj = Load(context, fileName, directory, fileExtension, env, isSharedStorage)
    fun load(): Bitmap? = loadObj.load()
    fun load(block: (bitmap: Bitmap?) -> Unit, catch: ((e: Exception) -> Unit)? = null) {
        loadObj.load(object: LoadImageCallback {
            override fun onResult(bitmap: Bitmap?) { block.invoke(bitmap) }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }
    fun loadUri(activity: AppCompatActivity, appId: String): Uri? =
        loadObj.loadUri(activity, appId)

    /**
     *  Save Region
     */
    private val saveObj = Save(context, fileName, directory, fileExtension, env, isSharedStorage)
    fun save(bitmap: Bitmap, quality: Int): Boolean = saveObj.save(bitmap, quality)
    fun save(bitmap: Bitmap, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        saveObj.save(bitmap, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
    fun getOutStreamPublic(block: (outputStream: OutputStream) -> Unit, catch: ((e: Exception) -> Unit)? = null){
        saveObj.getRawOutStream(object : OutStreamCallback {
            override fun onSuccess(outputStream: OutputStream) {block.invoke(outputStream)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
}

