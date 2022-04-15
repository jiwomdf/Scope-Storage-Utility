[![](https://jitpack.io/v/jiwomdf/EasyImageLibrary.svg)](https://jitpack.io/#jiwomdf/EasyImageLibrary)


# Android Image Util
Android Image Util is a library to help you converting and managing your image. <br>
the benefit of this library are
1. easy to use <br>
2. provide shared and private storage image management <br>
3. provide synchronous & asynchronous solution <br>
4. safe from throwing an error, the error can be trace by searching the logcat of <b>"AndroidImageUtil"</b> <br>

![](https://github.com/jiwomdf/ImageHarpa/blob/master/androidimageutil/gif/AndroidImageUtilApp.gif)

## Features
1. Convert image <br>
  a. convert bitmap to base64 <br>
  b. convert base64 to Bitmap <br>
  c. convert base64 to Drawable <br>
  d. convert drawable to Bitmap <br>
  e. convert drawable to base64 <br>
  
2. Manage image <br>
  a. load image <br>
  b. delete image <br>
  c. save bitmap formatted image <br>
  d. save base64 formatted image <br>
  e. save drawable formatted image <br>
  f. save drawable formatted image <br>


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
	        implementation 'com.github.jiwomdf:AndroidImageUtil:1.0.2'
	}
```
## Prerequirement
Please include this permission in your application <br>
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```

## Usage
In this example I will just show the basic of how to use the library, the all feature can be found when you try it! <br>
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

### For more features & information, you can check / clone the this repository

### On Going Development
1. add unit test, instrumented test, and CICD
2. Write more detail documentation about this lib
3. Add Compress, Black White, Rotate, and other image processing Utilities

<br><br>
Feel free to see and contribute
