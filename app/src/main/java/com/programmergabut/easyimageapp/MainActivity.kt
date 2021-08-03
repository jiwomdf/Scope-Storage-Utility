package com.programmergabut.easyimageapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.programmergabut.easyimage.EasyImage
import com.programmergabut.easyimage.Event
import com.programmergabut.easyimage.Extension
import com.programmergabut.easyimage.manage.IManageImage
import com.programmergabut.easyimageapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 1001
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //dispatchTakePictureIntent()
        EasyImage.Manage(this)
            .setFileAttribute("asd", "ad", Extension.JPEG)
            .save("asdas", 100, object: IManageImage.SaveBase64CallBack {
                override fun onSuccess() {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                }
                override fun onFailed(err: String) {
                    Toast.makeText(this@MainActivity, err, Toast.LENGTH_SHORT).show()
                }
            })

        /* EasyImage.Manage(this)
            .setFileAttribute("as","asd",Extension.JPEG)
            .delete(object: IManageImage.DeleteCallBack {
                override fun onSuccess() {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
                }
                override fun onFailed(err: String) {
                    Toast.makeText(this@MainActivity, err, Toast.LENGTH_SHORT).show()
                }
            }) */
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

    private fun proceedData(data: Intent?) {
        val captureImage = data?.extras!!["data"] as Bitmap
        EasyImage.Convert.bitmap.bitmapToBase64(captureImage)
        EasyImage.Convert.bitmap.bitmapToBase64.observe(this, {
          when(it){
              is Event.Success -> {
                  binding.pgLoading.visibility = View.GONE
                  Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
              }
              is Event.Loading -> {
                  binding.pgLoading.visibility = View.VISIBLE
              }
              is Event.Error -> {
                  binding.pgLoading.visibility = View.GONE
                  Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
              }
          }
        })
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
            requestPermissions(arrPermissionTakePhoto, TAKE_PHOTO_REQUEST_PERMISSION_CODE)
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(this.packageManager)?.also {
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE)
                }
            }
        }
    }

}