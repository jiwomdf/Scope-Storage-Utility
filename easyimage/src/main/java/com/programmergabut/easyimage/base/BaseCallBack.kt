package com.programmergabut.easyimage.base


interface BaseCallBack {
    fun onFailed(ex: Exception)
}

interface BaseCallBack2: BaseCallBack {
    fun onSuccess()
}

