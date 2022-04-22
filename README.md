[![](https://jitpack.io/v/jiwomdf/Android-Image-Util.svg)](https://jitpack.io/#jiwomdf/Android-Image-Util)


# Android Image Util
Android Image Util is a library to help you converting and managing your image. <br>
the benefit of this library are
1. easy to use ✅<br>
2. provide shared and private storage image management ✅<br>
3. provide synchronous & asynchronous solution ✅<br>
4. safe from throwing an error, the error can be trace by searching the logcat of <b>"AndroidImageUtil"</b> ✅<br>
5. for more detail info, see the full doc here [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md)



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


## Full Documentation of The Library Can Be Found Here👋
➡️ [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md) ⬅️

<br>

## How to download the lib
Add jitpack package repository in your root build.gradle at the allprojects inside repositories:
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

<br>
Feel free to see and contribute
