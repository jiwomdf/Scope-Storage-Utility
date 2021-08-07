package com.programmergabut.easyimageapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.programmergabut.easyimage.EasyImage.Companion.convert
import com.programmergabut.easyimage.EasyImage.Companion.manage
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimageapp.databinding.ActivityMainBinding

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

        val test = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG)
        manage(applicationContext)
            .imageAttribute("testong", null, Extension.PNG)
            .save(test!!,100)
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