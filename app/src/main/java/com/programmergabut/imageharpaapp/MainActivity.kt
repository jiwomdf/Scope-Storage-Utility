package com.programmergabut.imageharpaapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.programmergabut.imageharpa.Extension
import com.programmergabut.imageharpa.ImageHarpa.Companion.convert
import com.programmergabut.imageharpa.ImageHarpa.Companion.manage
import com.programmergabut.imageharpaapp.databinding.ActivityMainBinding
import com.programmergabut.imageharpa.convert.Base64Callback
import com.programmergabut.imageharpa.manage.ImageCallback

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main){

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 1001
    }

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

        val base64 = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG) ?: return

        convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG, object:
            Base64Callback {
            override fun onResult(base64: String) {
                Toast.makeText(this@MainActivity, "Success Convert", Toast.LENGTH_SHORT).show()
            }
            override fun onFailed(ex: Exception) {
                Toast.makeText(this@MainActivity, ex.message, Toast.LENGTH_SHORT).show()
            }
        })

        manage(this)
            .imageAttribute("test", "testing/testing/", Extension.PNG)
            .save(base64, 100, object : ImageCallback {
                override fun onSuccess() {
                    Toast.makeText(this@MainActivity, "Success Load", Toast.LENGTH_SHORT).show()
                }
                override fun onFailed(ex: Exception) {
                    Toast.makeText(this@MainActivity, ex.message, Toast.LENGTH_SHORT).show()
                }
            })

        manage(this)
            .imageAttribute("test2", "testing/testing/", Extension.PNG)
            .save(base64, 100)

        manage(this)
            .imageAttribute("test2",null, Extension.PNG)
            .getURI()
            .also {
                Log.d("MainActivity", "tryEasyImage: $it")
            }

        manage(this)
            .imageAttribute("test3","testing/testing/", Extension.PNG)
            .savePublic(base64, 100)
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