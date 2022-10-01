package com.programmergabut.androidimageutil.manage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.programmergabut.androidimageutil.manage.action.Delete
import com.programmergabut.androidimageutil.manage.action.Load
import com.programmergabut.androidimageutil.manage.action.Save
import com.programmergabut.androidimageutil.util.Extension
import com.programmergabut.androidimageutil.util.sdk29AndUp
import com.programmergabut.androidimageutil.util.setExtension

class ManageImage(
    context: Context,
    fileName: String,
    directory: String?,
    fileExtension: Extension,
    env: String = Environment.DIRECTORY_PICTURES
) {
    /**
     *  Delete Region
     */
    private val delete = Delete(context, fileName, directory, fileExtension, env)
    fun delete() = delete.d()
    fun delete(callBack: ImageCallback){
        delete.d(object: ImageCallback {
            override fun onSuccess() { callBack.onSuccess() }
            override fun onFailed(ex: Exception) { callBack.onFailed(ex) }
        })
    }
    fun delete(block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        delete.d(object: ImageCallback {
            override fun onSuccess() { block.invoke() }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }
    fun deletePublic(
        intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>
    ) = delete.dPublic(intentSenderRequest)
    fun deletePublic(
        intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>,
        callBack: ImageCallback
    ) {
        delete.dPublic(intentSenderRequest, object: ImageCallback {
            override fun onSuccess() { callBack.onSuccess() }
            override fun onFailed(ex: Exception) { callBack.onFailed(ex) }
        })
    }
    fun deletePublic(
        intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>,
        block: () -> Unit,
        catch: ((e: Exception) -> Unit)? = null,
    ) {
        delete.dPublic(intentSenderRequest, object: ImageCallback {
            override fun onSuccess() { block.invoke() }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }

    /**
     *  Load Region
     */
    private val load = Load(context, fileName, directory, fileExtension, env)
    fun load(): Bitmap? = load.l()
    fun load(callBack: LoadImageCallback) {
        load.l(object: LoadImageCallback {
            override fun onResult(bitmap: Bitmap?) { callBack.onResult(bitmap) }
            override fun onFailed(ex: Exception) { callBack.onFailed(ex) }
        })
    }
    fun load(block: (bitmap: Bitmap?) -> Unit, catch: ((e: Exception) -> Unit)? = null) {
        load.l(object: LoadImageCallback {
            override fun onResult(bitmap: Bitmap?) { block.invoke(bitmap) }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }
    fun loadPublic(): Bitmap? = load.lPublic()
    fun loadPublic(callBack: LoadImageCallback){
        load.lPublic(object: LoadImageCallback {
            override fun onResult(bitmap: Bitmap?) { callBack.onResult(bitmap) }
            override fun onFailed(ex: Exception) { callBack.onFailed(ex) }
        })
    }
    fun loadPublic(block: (bitmap: Bitmap?) -> Unit, catch: ((e: Exception) -> Unit)? = null){
        load.lPublic(object: LoadImageCallback {
            override fun onResult(bitmap: Bitmap?) { block.invoke(bitmap) }
            override fun onFailed(ex: Exception) { catch?.invoke(ex) }
        })
    }
    fun loadPublicUri(): Uri? = load.lPublicUri()

    /**
     *  Save Region
     */
    private val save = Save(context, fileName, directory, fileExtension, env)
    fun save(bitmap: Bitmap, quality: Int): Boolean = save.s(bitmap, quality)
    fun save(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback) {
        save.s(bitmap, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun save(base64: String, quality: Int): Boolean = save.s(base64, quality)
    fun save(base64: String, quality: Int, imageCallBack: ImageCallback){
        save.s(base64, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun save(base64: String, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        save.s(base64, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
    fun save(drawable: Drawable, quality: Int): Boolean = save.s(drawable, quality)
    fun save(drawable: Drawable, quality: Int, imageCallBack: ImageCallback){
        save.s(drawable, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun save(drawable: Drawable, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        save.s(drawable, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
    fun savePublic(bitmap: Bitmap, quality: Int): Boolean = save.sPublic(bitmap, quality)
    fun savePublic(bitmap: Bitmap, quality: Int, imageCallBack: ImageCallback){
        save.sPublic(bitmap, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun savePublic(bitmap: Bitmap, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        save.sPublic(bitmap, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
    fun savePublic(base64: String, quality: Int): Boolean = save.sPublic(base64, quality)
    fun savePublic(base64: String, quality: Int, imageCallBack: ImageCallback) {
        save.sPublic(base64, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun savePublic(base64: String, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        save.sPublic(base64, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
    fun savePublic(drawable: Drawable, quality: Int): Boolean = save.sPublic(drawable, quality)
    fun savePublic(drawable: Drawable, quality: Int, imageCallBack: ImageCallback){
        save.sPublic(drawable, quality, object : ImageCallback {
            override fun onSuccess() {imageCallBack.onSuccess()}
            override fun onFailed(ex: Exception) {imageCallBack.onFailed(ex)}
        })
    }
    fun savePublic(drawable: Drawable, quality: Int, block: () -> Unit, catch: ((e: Exception) -> Unit)? = null){
        save.sPublic(drawable, quality, object : ImageCallback {
            override fun onSuccess() {block.invoke()}
            override fun onFailed(ex: Exception) {catch?.invoke(ex)}
        })
    }
}

