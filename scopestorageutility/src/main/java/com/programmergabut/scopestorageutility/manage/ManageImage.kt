package com.programmergabut.scopestorageutility.manage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import com.programmergabut.scopestorageutility.manage.action.Delete
import com.programmergabut.scopestorageutility.manage.action.Load
import com.programmergabut.scopestorageutility.manage.action.OutStream
import com.programmergabut.scopestorageutility.manage.action.Save
import com.programmergabut.scopestorageutility.manage.callback.ImageCallback
import com.programmergabut.scopestorageutility.manage.callback.LoadImageCallback
import com.programmergabut.scopestorageutility.manage.callback.OutputStreamCallback
import com.programmergabut.scopestorageutility.util.Extension
import java.io.OutputStream

class ManageImage(
    context: Context,
    fileName: String,
    directory: String?,
    env: String = Environment.DIRECTORY_DCIM,
    fileExtension: Extension.ExtensionModel,
    isSharedStorage: Boolean,
) {

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
    fun loadSharedFileUri(activity: AppCompatActivity, appId: String): Uri? =
        loadObj.loadSharedFileUri(activity, appId)

    /**
     *  Delete Region
     */
    private val deleteObj = Delete(context, fileName, directory, fileExtension, env, isSharedStorage)
    fun delete(intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>) = deleteObj.delete(intentSenderRequest)
    fun delete(
        intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>? = null,
        block: () -> Unit,
        catch: ((e: Exception) -> Unit)? = null,
    ) {
        deleteObj.delete(intentSenderRequest, object: ImageCallback {
            override fun onSuccess() { block.invoke() }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }

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

    /**
     *  OutStream Region
     */
    private val outStream = OutStream(context, fileName, directory, fileExtension, env, isSharedStorage)
    fun getOutputStream() = outStream.getOutStream()
    fun getOutputStream(block: (outputStream: OutputStream) -> Unit, catch: ((e: Exception) -> Unit)? = null){
        outStream.getOutputStream(object : OutputStreamCallback {
            override fun onSuccess(outputStream: OutputStream) {block.invoke(outputStream)}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
}

