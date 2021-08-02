package com.programmergabut.easyimageapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>(
    private val layout: Int
): AppCompatActivity() {

    abstract fun getViewBinding(): VB
    private var _binding: ViewBinding? = null
    protected val binding: VB
        get() = _binding as VB


    protected val TAKE_PHOTO_REQUEST_PERMISSION_CODE = 1002

    // Camera Permission
    protected val arrPermissionTakePhoto = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = getViewBinding()
        setContentView(binding.root)
    }


    protected fun isCameraPermissionGranted() :Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
    }

    protected fun shouldShowCameraPermissionRationale() : Boolean {
        return (ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CAMERA
        ))
    }

    // SD Card Permission
    protected fun isReadWriteFilePermissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
    }

    protected fun shouldShowReadWriteFileGranted(): Boolean {
        return (ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ))
    }

}