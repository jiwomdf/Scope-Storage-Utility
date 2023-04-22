package com.programmergabut.scopestorageutilityapp

import android.os.Bundle
import android.widget.Toast
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

    fun showToast(msg: String) {
        Toast.makeText(this@BaseActivity, msg, Toast.LENGTH_SHORT).show()
    }

}