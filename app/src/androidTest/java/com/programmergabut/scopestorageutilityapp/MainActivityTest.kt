package com.programmergabut.scopestorageutilityapp

import android.app.Instrumentation
import android.os.Environment
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.programmergabut.scopestorageutilityapp.util.ImageViewHasDrawableMatcher.hasDrawable
import com.programmergabut.scopestorageutilityapp.util.createImageCaptureActivityResultStub
import com.programmergabut.scopestorageutilityapp.util.getText
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

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

    @Test
    fun testSaveLoadAndDeletePhotoPrivateAndPublic_Success() {
        Intents.init()
        val result: Instrumentation.ActivityResult = createImageCaptureActivityResultStub()
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        val cleanDirectory = "folder/subfolder"
        val externalStorageDirectory = "${Environment.DIRECTORY_DCIM}${File.separator}$cleanDirectory"
        val imagePath = Environment.getExternalStoragePublicDirectory(externalStorageDirectory).absolutePath

        onView(withId(R.id.iv_image_1)).check(matches(not(hasDrawable())))
        onView(withId(R.id.iv_image_2)).check(matches(not(hasDrawable())))

        onView(withId(R.id.btn_dispatch_camera)).perform(click())

        onView(withId(R.id.iv_image_1)).check(matches(hasDrawable()))
        onView(withId(R.id.iv_image_2)).check(matches(hasDrawable()))

        val file = File(imagePath, "test.png")
        assertTrue(!file.exists())

        Intents.release()
    }

    @Test
    fun testCreateFile_Success() {
        val cleanDirectory = "folder/subfolder"
        val externalStorageDirectory = "${Environment.DIRECTORY_DOWNLOADS}${File.separator}$cleanDirectory"
        val imagePath = Environment.getExternalStoragePublicDirectory(externalStorageDirectory).absolutePath

        onView(withId(R.id.btn_create_file))
            .perform(scrollTo(), click())

        onView(withId(R.id.btn_create_file)).perform(click())

        val file = File(imagePath, "somefile.txt")
        assertTrue(file.exists())

        /** Cleanup folder **/
        val parentExternalStorageDirectory = "${Environment.DIRECTORY_DOWNLOADS}${File.separator}folder"
        val filePathParent = Environment.getExternalStoragePublicDirectory(parentExternalStorageDirectory).absolutePath
        File(filePathParent).deleteRecursively()
    }

    @Test
    fun testCreateFile_UpdateEnvDirectoryAndName_Success() {
        val fileName = "test2"
        onView(withId(R.id.btn_create_file))
            .perform(scrollTo(), click())

        onView(withId(R.id.et_file_dir))
            .perform(clearText(), typeText("folder2/subfolder2"))
        onView(withId(R.id.et_file_name))
            .perform(clearText(), typeText(fileName))

        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())

        val cleanDirectory = onView(withId(R.id.et_file_dir)).getText()
        val externalStorageDirectory = "${Environment.DIRECTORY_DOWNLOADS}${File.separator}$cleanDirectory"
        val filePath = Environment.getExternalStoragePublicDirectory(externalStorageDirectory).absolutePath

        onView(withId(R.id.btn_create_file)).perform(click())

        val file = File(filePath, "${fileName}.txt")
        assertTrue(file.exists())

        /** Cleanup folder **/
        val parentExternalStorageDirectory = "${Environment.DIRECTORY_DOWNLOADS}${File.separator}folder2"
        val filePathParent = Environment.getExternalStoragePublicDirectory(parentExternalStorageDirectory).absolutePath
        File(filePathParent).deleteRecursively()
    }


}