package com.programmergabut.easyimage.base


interface BaseCallBack {
    fun onFailed(err: String)
}

interface BaseCallBack2: BaseCallBack {
    fun onSuccess()
}

