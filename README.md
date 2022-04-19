[![](https://jitpack.io/v/jiwomdf/Android-Image-Util.svg)](https://jitpack.io/#jiwomdf/Android-Image-Util)


# Android Image Util
Android Image Util is a library to help you converting and managing your image. <br>
the benefit of this library are
1. easy to use ✅<br>
2. provide shared and private storage image management ✅<br>
3. provide synchronous & asynchronous solution ✅<br>
4. safe from throwing an error, the error can be trace by searching the logcat of <b>"AndroidImageUtil"</b> ✅<br>

![](https://github.com/jiwomdf/ImageHarpa/blob/master/androidimageutil/gif/AndroidImageUtilApp.gif)

## Features  
| Convert image | Manage image  |
| :---   | :--- |
| convert bitmap to base64 | load image |
| convert base64 to Bitmap | delete image |
| convert base64 to Drawable | save bitmap formatted image |
| convert drawable to Bitmap | save base64 formatted image |
| convert drawable to base64 | save drawable formatted image |
| | save drawable formatted image |
| | get public image URI |


### For the full documentation of features above, you can find it here👋
➡️ [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md) ⬅️

<br>

## How to download the lib
1. Add jitpack package repository in your root build.gradle at the allprojects inside repositories:
```kotlin
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```
Add the dependency
```kotlin
	dependencies {
		...
	        implementation 'com.github.jiwomdf:Android-Image-Util:1.0.2'
	}
```
## Prerequirement
Please include this permission in your application <br>
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```

## Usage
In this example I will just show the basic of how to use the library, the all feature can be found here ➡️ [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md) ⬅️ <br>
So there are basically one static variable **convert** for converting features and one static function **manage(context)** for managing features. <br>

### Convert Image 
```kotlin
  val captureImage = bitmapImg // Image in bitmap format
  
  /* synchronously */
  val base64 = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG) 
  
  /* asynchronously */
  convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG, 
      object : Base64Callback {
          override fun onResult(base64: String) {
		//your code
          }

          override fun onFailed(ex: Exception) {
		//your code
          }
  })
```

### Manage image 
#### 1. Private storage
The image will be saved, deleted, and loaded from the internal application storage location, in
```
your_phone_name\Android\data\application_package_name\files\Pictures\
```
```kotlin

  /* synchronously */
  manage(this)
      .imageAttribute("test", "myfolder/subfolder", Extension.PNG)
      .save(base64,100)
      
  /* asynchronously */
  manage(this)
      .imageAttribute("test", "myfolder/subfolder", Extension.PNG)
      .save(base64, 100, object : ImageCallback {
          override fun onSuccess() {
		//your code
          }
          override fun onFailed(ex: Exception) {
		//your code
          }
      })
```
#### 2. Public storage
we can also save & load the image to public storage, so the image will be visible in galery </b>
The location of the image will be save in DCIM
```kotlin

  /* synchronously */
  manage(this)
      .imageAttribute("test", "myfolder/subfolder", Extension.PNG)
      .savePublic(base64,100)
      
  /* asynchronously */
  manage(this)
      .imageAttribute("test", "myfolder/subfolder", Extension.PNG)
      .savePublic(base64, 100, object : ImageCallback {
          override fun onSuccess() {
		//your code
          }
          override fun onFailed(ex: Exception) {
		//your code
          }
      })
```

### Information 
1. For syncronize funtion, it will always returned <b>true</b> if the process is success and <b>false</b> if it failed
2. For tracing the error you can try by searching the logcat of <b>"AndroidImageUtil"</b>
3. For better understanding about this lib, you can check / clone this repository and see the MainActivity file
4. To see all feature & how to use it, you can check it here ➡️ [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md) ⬅️ <br>

### On Going Development
1. Add unit test, instrumented test, and CICD
2. Add Compress, Black White, Rotate, and other image processing Utilities

<br><br>
Feel free to see and contribute
