# Android Image Util Docs

## Table of Contents
1. [Convert Image Format (bitmap, base64, drawable)](#convert)
2. [Manage Image Storage](#manage) <br>
  a. [Save](#save) <br>
  b. [Load](#load) <br>
  c. [Delete](#delete) <br>
  d. [Load Image Uri](#loaduri)

Before starting with the lib, here are some useful information
```
1. For syncronize funtion, it will always returned true if the process is success and false if it failed
2. For tracing the error you can try by searching the logcat of "AndroidImageUtil"
3. For better understanding about this lib, you can check / clone this repository and see the MainActivity file
```

## Convert Image Format (bitmap, base64, drawable)
#### Convert Bitmap to Base64 <a name="convert"></a>
```kotlin
  /**
   * @param bitmap is the image in bitmap format.
   * @param quality is the quality of the image, must be between 0 and 100.
   * @param format is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
   * @return string of base64 if success, or null if fail
   */

    /* Example of convert bitmap to base64 synchronously */
    return convert.bitmapToBase64(bitmap, 100, Bitmap.CompressFormat.PNG) ?: ""

    /* Example of convert bitmap to base64 asynchronously */
    convert.bitmapToBase64(bitmap, 100, Bitmap.CompressFormat.PNG, object: Base64Callback {
        override fun onResult(base64: String) {
            Log.d(TAG, "Success convert to base64")
        }
        override fun onFailed(ex: Exception) {
            Log.d(TAG, "Failed convert to base64")
        }
    })
  
   /**
   * for the format you can use
   */
   Bitmap.CompressFormat.JPEG, Bitmap.CompressFormat.PNG, Bitmap.CompressFormat.WEBP

```

#### Convert Base64 to Bitmap
```kotlin
  /**
   * convert base64 to Bitmap
   * @param base64 is the image in base64 string format.
   * @param offset is a number of values to skip before the first color in the array of colors.
   */
   
   /* Example of convert base64 to Bitmap synchronously */
   val bitmap = convert.base64ToBitmap(base64, 0)

   /* Example of convert base64 to Bitmap with asynchronously */
   convert.base64ToBitmap(base64, 0, object: BitmapCallback {
       override fun onResult(bitmap: Bitmap) {
           Log.d(TAG, "bitmap: $bitmap")
       }
       override fun onFailed(ex: Exception) {
           Log.d(TAG, "ex: $ex")
       }
   })
```

#### Convert Base64 To Drawable
```kotlin
  /**
   * convert base64 to Drawable
   * @param base64 is the image in base64 string format.
   * @param offset is a number of values to skip before the first color in the array of colors.
   */
   
   /* Example of convert base64 to Drawable synchronously */
   val drawable = convert.base64ToDrawable(base64, 0)
        Log.e(TAG, "drawable: $drawable")

   /* Example of convert base64 to Drawable asynchronously */
   convert.base64ToDrawable(base64, 0, object: DrawableCallback {
       override fun onResult(drawable: Drawable) {
           Log.e(TAG, "drawable: $drawable")
       }
       override fun onFailed(ex: Exception) {
           Log.e(TAG, "drawable: $ex")
       }
   })
```

#### Convert Drawable To Bitmap
```kotlin
  /**
   * convert drawable to Bitmap
   * @param drawable is the image in drawable.
   */
   
   /* Example of convert drawable to bitmap synchronously */
   val bitmap = convert.drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!)
   Log.e(TAG, "bitmap: $bitmap")

   /* Example of convert drawable to bitmap asynchronously */
   convert.drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!, object: BitmapCallback{
       override fun onResult(bitmap: Bitmap) {
           Log.e(TAG, "bitmap: $bitmap")
       }
       override fun onFailed(ex: Exception) {
           Log.e(TAG, "ex: $ex")
       }
   })
```

#### Convert Drawable to Base64
```kotlin
  /**
   * convert drawable to base64
   * @param drawable is the image in drawable.
   */
   
   /* Example of convert drawable to base64 synchronously */
   val base642 = convert.drawableToBase64(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!)
        Log.e(TAG, "base64: $base642")

   /* Example of convert drawable to base64 asynchronously */
   convert.drawableToBase64(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!, object: Base64Callback{
       override fun onResult(base64: String) {
           Log.e(TAG, "base64: $base64")
       }
       override fun onFailed(ex: Exception) {
           Log.e(TAG, "ex: $ex")
       }
   })
```
<br>

## Manage Image Storage <a name="manage"></a>
#### Manage function
```kotlin
  /**
   *  Set the attribute file you want to save or load
   *  @param fileName is the name of your file.
   *  @param directory is the directory of the file you want to save or load,
   *  if it is null or empty, it will saved to your absolute path.
   *  @param fileExtension is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
   */
   fun imageAttribute(fileName: String, directory: String?, fileExtension: Extension): ManageImage
   
   /**
   * for the fileExtension you can use
   */
   Extension.PNG, Extension.JPEG, Extension.JPG, Extension.WEBP
   
```

#### Save function (bitmap, base64, drawable) <a name="save"></a>
```kotlin
  /***
   * Example of saving base64 to internal storage 
   * in your_phone_name\Android\data\application_package_name\files\Pictures\
   */
  /* Example of save internal storage asynchronously */
  val isSuccess = manage(this)
      .imageAttribute("test", "folder/subfolder/", Extension.PNG)
      .save(base64, 100)
      
  /* Example of save internal storage asynchronously */
  manage(this)
      .imageAttribute("test", "folder/subfolder/", Extension.PNG)
      .save(base64, 100, object : ImageCallback{
          override fun onSuccess() {
            //your code
          }
          override fun onFailed(ex: Exception) {
            //your code
          }
      })
      
      
  /***
   * Example of save public storage in DCIM folder
   */
  
  /* Example of save public storage synchronously */
  manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .savePublic(base64, 100)
  
  /* Example of save public storage asynchronously */
  manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .savePublic(base64, 100, object : ImageCallback {
          override fun onSuccess() {
              //your code
          }
          override fun onFailed(ex: Exception) {
              //your code
          }
      })
```

#### Load function <a name="load"></a>
```kotlin
  /* Example of load internal storage synchronously */
  val bitmap = manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .load()
            
  /* Example of load internal storage asynchronously */
  manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .load(object : LoadImageCallback {
          override fun onResult(bitmap: Bitmap?) {
              Log.d(TAG, "Success load image test")
          }
          override fun onFailed(ex: Exception) {
              Log.d(TAG, "Failed load image")
          }
      })
  
  /* Example of load public storage synchronously */
  val bitmap = manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .loadPublic()
  
  /* Example of load public storage asynchronously */
  manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .loadPublic(object : LoadImageCallback {
          override fun onResult(bitmap: Bitmap?) {
              Log.d(TAG, "Success load image test_public")
          }
          override fun onFailed(ex: Exception) {
              Log.d(TAG, "Failed load image")
          }
      })
```
#### Delete function <a name="delete"></a>
```kotlin

  /* Example of delete internal storage asynchronously */ 
  val isDeleted = manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .delete()
      
  /* Example of delete internal storage asynchronously */ 
  manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .delete(object : ImageCallback {
          override fun onSuccess() {
              Log.d(TAG, "Success delete image test")
          }
          override fun onFailed(ex: Exception) {
              Log.d(TAG, "Failed delete image")
          }
      })
    
  /* Example of delete public storage asynchronously */ 
  val isDeleted = manage(this)
      .imageAttribute(imageFile, imageDir, Extension.JPG)
      .deletePublic(intentSenderRequest)
      
  /* Example of delete internal storage asynchronously */ 
  manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .delete(object : ImageCallback {
          override fun onSuccess() {
              Log.d(TAG, "Success delete image test")
          }
          override fun onFailed(ex: Exception) {
              Log.d(TAG, "Failed delete image")
          }
      })
      
  /**
    * for delete public image you need intentSenderRequest
    */
    intentSenderRequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
          if (it.resultCode == RESULT_OK) {
              if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                  /** this line of code will arrive here if the user allow to delete file that's not this app create */
                  deletePublicImage(imageFile, imageDir)
              }
          } else {
              Log.d(TAG, "Failed delete public image")
          }
      }
```

#### Load Image URI (for public image) <a name="loaduri"></a>
```kotlin
    /***
     * Example getting the image URI of public image
     */
    manage(this)
        .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
        .loadPublicUri()
        .also {
            Log.d(TAG, "uri: $it")
        }
```
