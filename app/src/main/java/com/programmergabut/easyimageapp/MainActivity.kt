package com.programmergabut.easyimageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.programmergabut.easyimage.EasyImage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = EasyImage.Manage(this).loadFile()
        
    }
}