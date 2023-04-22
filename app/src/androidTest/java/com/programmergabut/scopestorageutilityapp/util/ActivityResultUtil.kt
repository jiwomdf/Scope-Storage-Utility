package com.programmergabut.scopestorageutilityapp.util

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.programmergabut.scopestorageutilityapp.R

fun createImageCaptureActivityResultStub(): Instrumentation.ActivityResult {
    val image = BitmapFactory.decodeResource(
        InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.test)
    val intent = Intent().putExtra("data", image)
    return Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
}