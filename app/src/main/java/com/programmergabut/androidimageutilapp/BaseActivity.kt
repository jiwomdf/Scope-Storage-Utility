package com.programmergabut.androidimageutilapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>(
    private val layout: Int
): AppCompatActivity() {

    abstract fun getViewBinding(): VB
    private var _binding: ViewBinding? = null
    protected val binding: VB
        get() = _binding as VB

    lateinit var permissionUtil: PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = getViewBinding()
        setContentView(binding.root)
        permissionUtil = PermissionUtil(this@BaseActivity)
    }

}