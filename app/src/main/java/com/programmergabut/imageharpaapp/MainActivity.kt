package com.programmergabut.imageharpaapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.programmergabut.imageharpa.ImageHarpa.Companion.convert
import com.programmergabut.imageharpa.ImageHarpa.Companion.manage
import com.programmergabut.imageharpa.convert.Base64Callback
import com.programmergabut.imageharpa.manage.ImageCallback
import com.programmergabut.imageharpa.manage.LoadImageCallback
import com.programmergabut.imageharpa.util.Extension
import com.programmergabut.imageharpaapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 1001
    }

    private val TAG = "TestMainActivity"

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnDispatchCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            TAKE_PHOTO_REQUEST_CODE -> {
                try {
                    tryEasyImage(data)
                } catch (ex: Exception){
                    Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun tryEasyImage(data: Intent?)  {
        val captureImage = data?.extras!!["data"] as Bitmap
        val base64 = convertSection(captureImage)
        internalStorageSection(base64)
        sharedStorageSection(base64)
    }

    private fun convertSection(captureImage: Bitmap): String {
        /***
         * Example of convert to base64 with callback
         */
        convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG, object:
            Base64Callback {
            override fun onResult(base64: String) {
                Log.d(TAG, "Success convert to base64", )
            }
            override fun onFailed(ex: Exception) {
                Log.d(TAG, "Failed convert to base64")
            }
        })

        /***
         * Example of convert to base64 without callback
         */
        return convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG) ?: ""
    }

    private fun internalStorageSection(base64: String) {
        /***
         * Example of saving base64 to internal storage
         */
        manage(this)
            .imageAttribute("test", "testing/testing/", Extension.PNG)
            .save(base64, 100)

        /***
         * Example of saving base64 to internal storage with callback
         */
        manage(this)
            .imageAttribute("test", "testing/testing/", Extension.PNG)
            .save(base64, 100, object : ImageCallback {
                override fun onSuccess() {
                    Log.d(TAG, "Success Save test to testing/testing/")
                }
                override fun onFailed(ex: Exception) {
                    Log.d(TAG, "Failed Save test to testing/testing/")
                }
            })

        /***
         * Example of load internal storage with callback
         */
        manage(this)
            .imageAttribute("test","testing/testing/", Extension.PNG)
            .load(object : LoadImageCallback {
                override fun onResult(bitmap: Bitmap?) {
                    Log.d(TAG, "Success Load image test")
                    Glide.with(applicationContext)
                        .load(bitmap)
                        .into(binding.ivImage1)
                }
                override fun onFailed(ex: Exception) {
                    Log.d(TAG, "Failed Load image")
                }
            })
    }

    private fun sharedStorageSection(base64: String) {
        /***
         * Example of save public storage
         */
        manage(this)
            .imageAttribute("test2","testing/testing/", Extension.PNG)
            .savePublic(base64, 100)

        /***
         * Example of save public storage with callback
         */
        manage(this)
            .imageAttribute("test2","testing/testing/", Extension.PNG)
            .savePublic(base64, 100, object: ImageCallback {
                override fun onSuccess() {
                    Log.d(TAG, "Success Save test2 to DCIM/testing/testing/")
                }
                override fun onFailed(ex: Exception) {
                    Log.d(TAG, "Failed Save test to DCIM/testing/testing/")
                }
            })

        /***
         * Example of load public storage with callback
         */
        manage(this)
            .imageAttribute("test2","testing/testing/", Extension.PNG)
            .loadPublic(object : LoadImageCallback {
                override fun onResult(bitmap: Bitmap?) {
                    Log.d(TAG, "Success Load image test2")
                    Glide.with(applicationContext)
                        .load(bitmap)
                        .into(binding.ivImage2)
                }
                override fun onFailed(ex: Exception) {
                    Log.d(TAG, "Failed Load image")
                }
            })

        /***
         * Example getting the image URI
         */
        manage(this)
            .imageAttribute("test4","testing/testing/", Extension.PNG)
            .loadPublicUri()
            .also {
                Log.d(TAG, "uri: $it")
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            TAKE_PHOTO_REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    dispatchTakePictureIntent()
                } else {
                    if (shouldShowReadWriteFileGranted() && shouldShowCameraPermissionRationale()){
                        Toast.makeText(this, "Please grant the permission", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Please grant the permission", Toast.LENGTH_SHORT).show()
                    }
                }
                return
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        if (!isCameraPermissionGranted() || !isReadWriteFilePermissionGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrPermissionTakePhoto, TAKE_PHOTO_REQUEST_PERMISSION_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrPermissionTakePhoto,
                    TAKE_PHOTO_REQUEST_PERMISSION_CODE)
            }
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(this.packageManager)?.also {
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE)
                }
            }
        }
    }

}