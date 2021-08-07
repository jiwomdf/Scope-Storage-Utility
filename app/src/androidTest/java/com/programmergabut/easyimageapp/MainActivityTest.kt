package com.programmergabut.easyimageapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.test.rule.ActivityTestRule
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.programmergabut.easyimage.EasyImage.Companion.convert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun testRecyclerViewBehaviour() {

        val drawable = ContextCompat.getDrawable(
            activityRule.activity.applicationContext, R.drawable.ic_android_black_24dp
        )
        val bitmap: Bitmap = BitmapFactory.decodeResource(
            activityRule.activity.applicationContext.resources,
            R.drawable.ic_android_black_24dp
        )

        val base64 = convert.bitmapToBase64(bitmap, 100, Bitmap.CompressFormat.PNG)
        val data1 = convert.base64ToBitmap(base64!!, 100)
        val data2 = convert.base64ToDrawable(base64, 100)

        val data3 = convert.drawableToBitmap(drawable!!)
        val data4 = convert.drawableToBase64(drawable)

        print("")
    }



}