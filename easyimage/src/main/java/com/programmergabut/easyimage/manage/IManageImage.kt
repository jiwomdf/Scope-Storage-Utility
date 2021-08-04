package com.programmergabut.easyimage.manage

import android.graphics.Bitmap
import com.programmergabut.easyimage.base.BaseCallBack2

interface IManageImage {
    interface LoadCallBack {
        fun onResult(bitmap: Bitmap?)
    }

    interface DeleteCallBack: BaseCallBack2

    interface SaveBitMapCallBack: BaseCallBack2

    interface SaveBase64CallBack: BaseCallBack2

    interface SaveDrawableCallBack: BaseCallBack2
}