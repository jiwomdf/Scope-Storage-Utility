package com.programmergabut.scopestorageutilityapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.programmergabut.scopestorageutilityapp.ImageViewHasDrawableMatcher.hasDrawable
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @JvmField
    @Rule
    var permissions: GrantPermissionRule =
        GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    @JvmField
    @Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun tearDown() {
        // Clears Intents state.
        Intents.release()
    }

    @Before
    fun stubCameraIntent() {
        Intents.init()
        val result: Instrumentation.ActivityResult = createImageCaptureActivityResultStub()

        // Stub the Intent.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.programmergabut.scopestorageutilityapp", appContext.packageName)
    }

    @Test
    fun testPhoto() {

        // Check that the ImageView doesn't have a drawable applied.
        onView(withId(R.id.iv_image_1)).check(matches(not(hasDrawable())))

        onView(withId(R.id.btn_dispatch_camera)).perform(click())

        onView(withId(R.id.btn_dispatch_camera)).perform(click())
        onView(withId(R.id.iv_image_1)).check(matches(hasDrawable()))
        Thread.sleep(5000)
    }

    private fun createImageCaptureActivityResultStub(): Instrumentation.ActivityResult {

        // Put the drawable in a bundle.
        val bundle = Bundle()
        bundle.putParcelable(
            "data", BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().targetContext.resources,
                R.drawable.ic_android_24dp
            )
        )

        // Create the Intent that will include the bundle.
        val resultData = Intent()
        resultData.putExtras(bundle)

        // Create the ActivityResult with the Intent.
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    /* TODO: CARA2 @Test
    fun testPhoto() {
        activityRule.scenario.onActivity { activity ->
            onView(withId(R.id.btn_dispatch_camera)).perform(click())
            val imgCaptureResult = createImageCaptureActivityResultStub(activity)
            intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(imgCaptureResult)
            onView(withId(R.id.btn_dispatch_camera)).perform(click())
            onView(withId(R.id.iv_image_1)).check(matches(hasImageSet()))
            Thread.sleep(5000)
        }
    }


    fun createImageGallerySetResultStub(activity: Activity): Instrumentation.ActivityResult {
        val bundle = Bundle()
        val parcels = ArrayList<Parcelable>()

        val resultData = Intent()
        val file = File(Environment.DIRECTORY_DCIM + "/folder/subfolder/", "test.png")
        val uri = Uri.fromFile(file)
        val parcelable1 = uri as Parcelable
        parcels.add(parcelable1)
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, parcels)

        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun hasImageSet(): BoundedMatcher<View, ImageView> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image set.")
            }

            override fun matchesSafely(imageView: ImageView): Boolean {
                return imageView.background != null
            }
        }
    } */

}