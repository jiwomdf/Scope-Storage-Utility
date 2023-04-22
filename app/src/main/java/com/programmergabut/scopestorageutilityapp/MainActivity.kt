package com.programmergabut.scopestorageutilityapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.programmergabut.scopestorageutility.AndroidImageUtil.Companion.manage
import com.programmergabut.scopestorageutility.util.Extension
import com.programmergabut.scopestorageutility.util.isUsingScopeStorage
import com.programmergabut.scopestorageutilityapp.databinding.ActivityMainBinding
import java.io.OutputStreamWriter

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){

    private var isPermissionsGranted = true
    private lateinit var intentSenderRequest: ActivityResultLauncher<IntentSenderRequest>

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            tryAndroidImageUtil(result.data)
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            if (!it.value) {
                showToast("$permissionName need to be granted")
                isPermissionsGranted = false
            }
        }
        if(isPermissionsGranted){
            showToast("all permission granted")
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askForPermission()
        setListener()

        intentSenderRequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                showToast("Photo deleted")
            } else {
                showToast("Photo not deleted")
            }
        }
    }

    private fun setListener() {
        with(binding){
            btnDispatchCamera.setOnClickListener {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(this@MainActivity.packageManager)?.also {
                        cameraLauncher.launch(takePictureIntent)
                    }
                }
            }
            btnDeleteImage.setOnClickListener {
                deletePublicImage(
                    imageFile = etImageFile.text.toString(),
                    imageDir = etImageDir.text.toString(),
                    env = Environment.DIRECTORY_DCIM,
                    fileExtension = Extension.get(etImageExtension.text.toString()),
                    isSharedStorage = true
                )
            }
            btnCreateFile.setOnClickListener {
                manage(this@MainActivity)
                    .isShareStorage(true)
                    .attribute(
                        fileName = etFileName.text.toString(),
                        directory = etFileDir.text.toString(),
                        env = Environment.DIRECTORY_DOWNLOADS,
                        extension = Extension.ExtensionModel(".txt", "text/plain"),
                    )
                    .getOutStreamPublic({
                        val outWriter = OutputStreamWriter(it)
                        outWriter.append("Some word here")
                        outWriter.close()
                        it.close()

                        showToast("Success write some txt file")
                    }, {
                        showToast("Failed write some txt file")
                    })
            }
        }
    }

    private fun tryAndroidImageUtil(data: Intent?)  {
        val captureImage = data?.extras!!["data"] as Bitmap
        privateStorageSection(captureImage)
        sharedStorageSection(captureImage)
    }

    private fun privateStorageSection(bitmap: Bitmap) {

        /***
         * Example of saving bitmap to private storage
         */
        manage(this)
            .isShareStorage(false)
            .attribute(
                fileName = "test",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .save(bitmap, 100)

        /***
         * Example of load private storage with callback
         */
        manage(this)
            .isShareStorage(false)
            .attribute(
                fileName = "test",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .load({
                Glide.with(applicationContext)
                    .load(it)
                    .into(binding.ivImage1)
                deleteImage()
            })
    }

    private fun sharedStorageSection(bitmap: Bitmap) {

        /***
         * Example of save bitmap to public storage
         */
        manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = "test_public",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG),
            )
            .save(bitmap, 100)

        /***
         * Example of load public storage with callback
         */
        manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = "test_public",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .load({
                showToast("Success load image test_public")

                Glide.with(applicationContext)
                    .load(it)
                    .into(binding.ivImage2)

                deletePublicImage(
                    imageFile = "test_public",
                    imageDir = "folder/subfolder/",
                    env = Environment.DIRECTORY_DCIM,
                    fileExtension = Extension.get(Extension.PNG),
                    isSharedStorage = true
                )
            },{
                showToast("Failed load image")
            })

        /***
         * Example getting the image URI
         */
        manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = "test_public",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .loadUri(this, BuildConfig.APPLICATION_ID)
            .also {
                showToast("uri: $it")
            }
    }

    private fun deleteImage() {
        /***
         * Example of delete private storage with callback
         */
        manage(this)
            .isShareStorage(false)
            .attribute(
                fileName = "test",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .delete(null, {
                showToast("Success delete image test")
            },{
                showToast("Failed delete image")
            })
    }

    private fun deletePublicImage(
        imageFile: String,
        imageDir: String,
        env: String,
        fileExtension: Extension.ExtensionModel,
        isSharedStorage: Boolean
    ) {
        /***
         * Example of deleting public image
         */
        manage(this)
            .isShareStorage(isSharedStorage)
            .attribute(
                fileName = imageFile,
                directory = imageDir,
                env = env,
                extension = fileExtension
            )
            .delete(intentSenderRequest, {
                showToast("Success delete image $imageDir$imageFile")
            },{
                showToast(it.message.toString())
            })
    }


    private fun askForPermission() {
        permissionUtil.apply {
            if(isUsingScopeStorage) {
                if (!isPermissionGrantedScopeStorage()) {
                    permissionLauncher.launch(arrPermissionTakePhotoScopeStorage)
                    return
                }
            } else {
                if (!isPermissionGranted()) {
                    permissionLauncher.launch(arrPermissionTakePhoto)
                    return
                }
            }
        }
    }

}