[![](https://jitpack.io/v/jiwomdf/Android-Image-Util.svg)](https://jitpack.io/#jiwomdf/Android-Image-Util)


# Android Image Util
Android Image Util is a library to help you converting and managing your image. <br>
the benefit of this library are
1. Easy to use ✅<br>
2. Provide <b>shared and private storage</b> image management <br>
```as now for the android API 30+ there is a new rule of saving data to public storage ```✅<br>
3. Provide synchronous & asynchronous solution ✅<br>
4. Safe from throwing an error <br>
```the error can be trace by searching the logcat of "AndroidImageUtil"``` ✅<br>
5. For more detail info, see the full doc here [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md)



![](https://github.com/jiwomdf/ImageHarpa/blob/master/androidimageutil/gif/AndroidImageUtilApp.gif)

## Features  
| Convert image | Manage image  |
| :---   | :--- |
| convert bitmap to base64 | load image (private & shared) |
| convert base64 to Bitmap | delete image (private & shared) |
| convert base64 to Drawable | save bitmap formatted image (private & shared) |
| convert drawable to Bitmap | save base64 formatted image (private & shared) |
| convert drawable to base64 | save drawable formatted image (private & shared) |
| | save drawable formatted image (private & shared) |
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
	        implementation 'com.github.jiwomdf:Android-Image-Util:1.0.3'
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
