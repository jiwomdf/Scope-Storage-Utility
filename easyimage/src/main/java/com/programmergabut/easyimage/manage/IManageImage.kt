package com.programmergabut.easyimage.manage

import android.graphics.Bitmap

interface IManageImage {
    interface LoadCallBack {
        fun onResult(bitmap: Bitmap?)
    }

    interface DeleteCallBack {
        fun onSuccess()
        fun onFailed(err: String)
    }

    interface SaveBitMapCallBack {
        fun onSuccess()
        fun onFailed(err: String)
    }

    interface SaveBase64CallBack {
        fun onSuccess()
        fun onFailed(err: String)
    }

    interface SaveDrawableCallBack {
        fun onSuccess()
        fun onFailed(err: String)
    }
}