package com.programmergabut.scopestorageutility

import android.content.Context
import com.programmergabut.scopestorageutility.manage.Manage

class AndroidImageUtil {
    companion object {
        const val TAG = "AndroidImageUtil"
        fun manage(context: Context) = Manage(context)
    }
}