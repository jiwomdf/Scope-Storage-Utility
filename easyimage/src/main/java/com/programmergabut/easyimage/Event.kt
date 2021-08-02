package com.programmergabut.easyimage

sealed class Event<out T> {
    class Success<out T>(val data: T): Event<T>()
    class Error(val message: String = ""): Event<Nothing>()
    object Loading: Event<Nothing>()
}
