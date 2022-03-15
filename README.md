[![](https://jitpack.io/v/jiwomdf/EasyImageLibrary.svg)](https://jitpack.io/#jiwomdf/EasyImageLibrary)


# ImageHarpaLibrary
ImageHarpaLibrary is a library to help you converting and managing your image. <br>
the benefit of this library are
1. easy to use <br>
2. provide synchronous & asynchronous solution <br>
3. safe from throwing an error. <br>

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
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```
Add the dependency
```
	dependencies {
		...
	        implementation 'com.github.jiwomdf:EasyImageLibrary:1.0.0'
	}
```
## Prerequirement
Please include this permission in your application <br>
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```

## Usage

There are basically one static variable **convert** for converting features and one static function **manage(context)** for managing features. <br>

### Convert Image 
```
  val captureImage = bitmapImg /* Image in bitmap format */
  
  /* synchronously */
  val base64 = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG) 
  
  /* asynchronously */
  convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG, 
      object : IConvertImage.Base64CallBack {
          override fun onResult(base64: String) {
		//your code
          }

          override fun onFailed(ex: Exception) {
		//your code
          }
  })
```

### Manage image 
The image will be saved, deleted, and loaded from the internal application storage location, in
```
your_phone_name\Android\data\application_package_name\files\Pictures\
```
```

  /* synchronously */
  manage(this)
      .imageAttribute("test", "myfolder", Extension.PNG)
      .save(base64!!,100)
      
  /* asynchronously */
  manage(this)
      .imageAttribute("test", "myfolder", Extension.PNG)
      .save(base64, 100, object : IManageImage.SaveBase64CallBack {
          override fun onSuccess() {
		//your code
          }
          override fun onFailed(ex: Exception) {
		//your code
          }
      })
```

### On Going Development
1. Save, delete, load image in shared storage <br>
2. Create the MANAGE_EXTERNAL_STORAGE feature
3. add unit test, instrumented test, and CICD

<br><br>
Feel free to see and contribute
