package com.programmergabut.scopestorageutilityapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.programmergabut.scopestorageutility.ScopeStorageUtility.Companion.manage
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
            startScopeStorageUtility(result.data)
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
                deleteImageOnSharedStorage(
                    imageFile = etImageFile.text.toString(),
                    imageDir = etImageDir.text.toString(),
                    env = Environment.DIRECTORY_DCIM,
                    fileExtension = Extension.get(etImageExtension.text.toString()),
                    isSharedStorage = true
                )
            }
            btnCreateFile.setOnClickListener {
                createFileSection(
                    filename = etFileName.text.toString(),
                    fileDir = etFileDir.text.toString()
                )
            }
        }
    }

    private fun startScopeStorageUtility(data: Intent?)  {
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
         * Example of save bitmap to shared storage
         */
        manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = "test_shared",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG),
            )
            .save(bitmap, 100)

        /***
         * Example of load shared storage with callback
         */
        manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = "test_shared",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .load({
                showToast("Success load image test_shared")

                Glide.with(applicationContext)
                    .load(it)
                    .into(binding.ivImage2)

                deleteImageOnSharedStorage(
                    imageFile = "test_shared",
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
                fileName = "test_shared",
                directory = "folder/subfolder/",
                env = Environment.DIRECTORY_DCIM,
                extension = Extension.get(Extension.PNG)
            )
            .loadSharedFileUri(this, BuildConfig.APPLICATION_ID)
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

    private fun deleteImageOnSharedStorage(
        imageFile: String,
        imageDir: String,
        env: String,
        fileExtension: Extension.ExtensionModel,
        isSharedStorage: Boolean
    ) {
        /***
         * Example of deleting image on shared storage
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

    private fun createFileSection(filename: String, fileDir: String) {
        manage(this@MainActivity)
            .isShareStorage(true)
            .attribute(
                fileName = filename,
                directory = fileDir,
                env = Environment.DIRECTORY_DOWNLOADS,
                extension = Extension.ExtensionModel(".txt", "text/plain"),
            )
            .getOutputStream({
                val outWriter = OutputStreamWriter(it)
                outWriter.append("this is shared storage text")
                outWriter.close()
                it.close()

                showToast("Success write txt file on /${fileDir}${filename} in shared storage")
            }, {
                showToast("Failed write txt file in share storage")
            })

        manage(this@MainActivity)
            .isShareStorage(false)
            .attribute(
                fileName = filename,
                directory = fileDir,
                env = Environment.DIRECTORY_DOWNLOADS,
                extension = Extension.ExtensionModel(".txt", "text/plain"),
            )
            .getOutputStream({
                val outWriter = OutputStreamWriter(it)
                outWriter.append("this is private storage text")
                outWriter.close()
                it.close()

                showToast("Success write txt file on /${fileDir}${filename} in private storage")
            }, {
                showToast("Failed write some txt file in private storage")
            })
    }

}