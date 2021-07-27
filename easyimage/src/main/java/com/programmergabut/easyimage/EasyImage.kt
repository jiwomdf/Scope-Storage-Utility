package com.programmergabut.easyimage

import android.content.Context
import id.kjsindonesia.ia.easyimage2.Extension
import id.kjsindonesia.ia.easyimage2.SaveImage

class EasyImage {

    companion object {
        fun convert() {

        }
    }

    class Builder(private val context: Context) {
        private var fromDir: String? = null
        private var toDir: String? = null
        private var extension: Extension? = null


        fun from(fromDir: String) = apply { this.fromDir = fromDir }

        fun to(toDir: String) = apply { this.toDir = toDir }

        fun extension(extension: Extension) = apply { this.extension = extension }

        fun save() {

            if (fromDir.isNullOrEmpty())
                throw NullPointerException()
            if (toDir.isNullOrEmpty())
                throw NullPointerException()
            if (extension == null)
                throw NullPointerException()

            SaveImage(context!!, fromDir!!, toDir!!, extension!!)
        }
    }

}