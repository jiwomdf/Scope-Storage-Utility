package com.programmergabut.androidimageutilapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.programmergabut.androidimageutil.AndroidImageUtil.Companion.manage
import com.programmergabut.androidimageutil.util.Extension
import com.programmergabut.androidimageutil.util.isUsingScopeStorage
import com.programmergabut.androidimageutilapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){

    companion object {
        private const val TAG = "TestMainActivity"
    }

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
                Toast.makeText(this@MainActivity,
                "$permissionName need to be granted", Toast.LENGTH_SHORT).show()
                isPermissionsGranted = false
            }
        }
        if(isPermissionsGranted){
            Toast.makeText(this@MainActivity,
                "all permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askForPermission()
        setListener()

        with(binding){
            intentSenderRequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                        /** this line of code will arrive here if the user allow to delete file that's not this app create */
                        deletePublicImage(
                            imageFile = etImageFile.text.toString(),
                            imageDir = etImageDir.text.toString(),
                            env = Environment.DIRECTORY_DCIM,
                            fileExtension = Extension.JPG,
                            isSharedStorage = true
                        )
                    }
                } else {
                    Log.d(TAG, "Failed delete public image")
                }
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
                    fileExtension = Extension.JPG,
                    isSharedStorage = true
                )
            }
        }
    }

    private fun tryAndroidImageUtil(data: Intent?)  {
        val captureImage = data?.extras!!["data"] as Bitmap
        internalStorageSection(captureImage)
        sharedStorageSection(captureImage)
    }

    private fun internalStorageSection(bitmap: Bitmap) {
        /***
         * Example of saving base64 to internal storage
         */
        manage(this)
            .imageAttribute("test", "folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = false)
            .save(bitmap, 100)

        /***
         * Example of load internal storage with callback
         */
        manage(this)
            .imageAttribute("test","folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = false)
            .load({
                Glide.with(applicationContext)
                    .load(it)
                    .into(binding.ivImage1)
                deleteImage()
            })
    }

    private fun sharedStorageSection(bitmap: Bitmap) {
        /***
         * Example of save public storage
         */
        manage(this)
            .imageAttribute("test_public","folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = true)
            .save(bitmap, 100)

        /***
         * Example of load public storage with callback
         */
        manage(this)
            .imageAttribute("test_public","folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = true)
            .load({
                Log.d(TAG, "Success load image test_public")
                Glide.with(applicationContext)
                    .load(it)
                    .into(binding.ivImage2)

                deletePublicImage("test_public", "folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = true)
            },{
                Log.d(TAG, "Failed load image")
            })

        /***
         * Example getting the image URI
         */
        manage(this)
            .imageAttribute("test_public","folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = true)
            .loadUri(this, BuildConfig.APPLICATION_ID)
            .also {
                Log.d(TAG, "uri: $it")
            }
    }

    private fun deleteImage() {
        /***
         * Example of delete internal storage with callback
         */
        manage(this)
            .imageAttribute("test","folder/subfolder/", Environment.DIRECTORY_DCIM, Extension.PNG, isSharedStorage = false)
            .delete(null, {
                Log.d(TAG, "Success delete image test")
            },{
                Log.d(TAG, "Failed delete image")
            })
    }

    private fun deletePublicImage(
        imageFile: String,
        imageDir: String,
        env: String,
        fileExtension: Extension,
        isSharedStorage: Boolean
    ) {
        /***
         * Example of deleting public image
         */
        manage(this)
            .imageAttribute(
                fileName = imageFile,
                directory = imageDir,
                env = env,
                fileExtension = fileExtension,
                isSharedStorage = isSharedStorage
            )
            .delete(intentSenderRequest, {
                Log.d(TAG, "Success delete image $imageDir/$imageFile")
            },{
                Log.d(TAG, it.message.toString())
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