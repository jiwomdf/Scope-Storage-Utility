[![](https://jitpack.io/v/jiwomdf/Android-Image-Util.svg)](https://jitpack.io/#jiwomdf/Android-Image-Util)


# Scope Storage Utility
Welcome to Scope Storage Utility Library<br>
This library will help manage your image and file from the <b>Private and Shared</b> storage location and also it handle the <b>Android Scope Storage Policy.</b>
<br>
<br>
So now you dont have to worry anymore about the struggle of managing file in <b>private or shared storage in different sdk</b><br>
<br>
the benefit of this library are
1. Easy to use ✅<br>
2. Provide <b>shared and private storage</b> file management and also handle the <b> scope storage </b> policy <br>
```as now for the android API 30+ there is a new rule of saving data to shared storage ```✅<br>
3. Provide synchronous & asynchronous solution ✅<br>
4. Safe from throwing an error <br>
```the error can be trace by search the logcat of "ScopeStorageUtility"``` ✅<br>
5. For more detail info, see the full doc here [Full Documentation Link](https://github.com/jiwomdf/Android-Image-Util/blob/master/doc.md)

<img src="https://github.com/jiwomdf/Scope-Storage-Utility/blob/master/scopestorageutility/gif/ScopeStorageUtilityGif.gif" width="220" height="450"/>

## Features  
| Bitmap  |
| :--- |
| save bitmap (private & shared) |
| delete bitmap (private & shared) |
| load bitmap (private & shared) |
| get public file URI |

| File  |
| :--- |
| load outstream for file creation (private & shared) |
| delete file (private & shared) |
| get public file URI |

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
	        implementation 'com.github.jiwomdf:Android-Image-Util:1.0.4'
	}
```
## Prerequirement
Please include this permission in your application <br>
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```
```For android Tiramisu use READ_MEDIA_IMAGES instead of READ_EXTERNAL_STORAGE```

## Quick view how to use the lib
This is the example of load png file in shared storage, it already handle loading png 
using media query for scope storage device and using the Environment class for non scope storage device <br>
```kotlin
    manage(this)
        .isShareStorage(true)
        .attribute(
            fileName = "test_shared",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
        )
        .load({ bitmap ->
            //success
        },{
            //failed
        })
```

<br>
Feel free to see and contribute
