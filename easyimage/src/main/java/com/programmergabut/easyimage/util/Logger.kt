package com.programmergabut.easyimage.util

import android.os.Build
import android.os.Debug
import android.util.Log
import com.programmergabut.easyimage.BuildConfig

object Logger {

    fun logE(tag: String, msg: String){
        if(BuildConfig.DEBUG){
            Log.e(tag, msg)
        }
    }

}