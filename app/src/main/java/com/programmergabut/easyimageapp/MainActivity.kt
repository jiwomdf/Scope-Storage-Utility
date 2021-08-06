package com.programmergabut.easyimageapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.programmergabut.easyimage.EasyImage.Companion.convert
import com.programmergabut.easyimage.EasyImage.Companion.manage
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.convert.IConvertBitmap
import com.programmergabut.easyimage.manage.IManageImage
import com.programmergabut.easyimageapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), View.OnClickListener {

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 1001
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnDispatchCamera.setOnClickListener(this)
        /* convert.base64ToDrawable("", 0, object: IConvertBitmap.DrawableCallBack {
            override fun onResult(drawable: Drawable) {

            }

            override fun onFailed(err: String) {

            }
        }) */
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_dispatch_camera -> {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            TAKE_PHOTO_REQUEST_CODE -> {
                try {
                    proceedData(data)
                } catch (ex: Exception){
                    Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun proceedData(data: Intent?)  {
        val captureImage = data?.extras!!["data"] as Bitmap

        /* convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG,
            object : IConvertBitmap.Base64CallBack {
                override fun onResult(base64: String) {
                    Toast.makeText(this@MainActivity, base64, Toast.LENGTH_SHORT).show()
                }

                override fun onFailed(err: String) {
                    Toast.makeText(this@MainActivity, err, Toast.LENGTH_SHORT).show()
                }
            }
        ) */

        val test = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG)
        manage(this)
            .imageAttribute("testong", null, Extension.JPEG)
            .save(test!!, 100)

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