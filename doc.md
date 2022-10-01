# Android Image Util Docs

## Table of Contents
1. [Convert Image Format (bitmap, base64, drawable)](#convert) <br>
  a. [Convert Bitmap to Base64](#convert_bitmap_to_base64) <br>
  b. [Convert Base64 to Bitmap](#convert_base64_to_bitmap) <br>
  c. [Convert Base64 to Drawable](#convert_base64_to_drawable) <br>
  d. [Convert Drawable to Bitmap](#convert_drawable_to_bitmap) <br>
  e. [Convert Drawable to Base64](#convert_drawable_to_base64)

2. [Manage Image Storage](#manage) <br>
  a. [Save Private And Public Image](#save) <br>
  b. [Load Private And Public Image](#load) <br>
  c. [Delete Private And Public Image](#delete) <br>
  d. [Load Image Uri of Public Image](#loaduri)

Before starting with the lib, here are some useful information
```
1. For syncronize funtion, it will always returned true if the process is success and false if it failed
2. For tracing the error you can try by searching the logcat of "AndroidImageUtil"
3. For better understanding about this lib, you can check / clone this repository and see the MainActivity file
```

## Convert Image Format (bitmap, base64, drawable) <a name="convert"></a>
#### Convert Bitmap to Base64 <a name="convert_bitmap_to_base64"></a>
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
    convert.bitmapToBase64(bitmap, 100, Bitmap.CompressFormat.PNG, {
        Log.d(TAG, "Success convert to base64")
    },{
        Log.d(TAG, "Failed convert to base64")
    })
  
   /**
   * for the format you can use
   */
   Bitmap.CompressFormat.JPEG, Bitmap.CompressFormat.PNG, Bitmap.CompressFormat.WEBP

```

#### Convert Base64 to Bitmap <a name="convert_base64_to_bitmap"></a>
```kotlin
  /**
   * convert base64 to Bitmap
   * @param base64 is the image in base64 string format.
   * @param offset is a number of values to skip before the first color in the array of colors.
   */
   
   /* Example of convert base64 to Bitmap synchronously */
   val bitmap = convert.base64ToBitmap(base64, 0)

   /* Example of convert base64 to Bitmap with asynchronously */
   convert.base64ToBitmap(base64, 0,{
      Log.d(TAG, "bitmap: $bitmap")
   },{
      Log.d(TAG, "ex: $ex")  
   })
```

#### Convert Base64 To Drawable <a name="convert_base64_to_drawable"></a>
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
   convert.base64ToDrawable(base64, 0, {
       Log.e(TAG, "drawable: $drawable")
   },{
       Log.e(TAG, "drawable: $ex")
   })
```

#### Convert Drawable To Bitmap <a name="convert_drawable_to_bitmap"></a>
```kotlin
  /**
   * convert drawable to Bitmap
   * @param drawable is the image in drawable.
   */
   
   /* Example of convert drawable to bitmap synchronously */
   val bitmap = convert.drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!)
   Log.e(TAG, "bitmap: $bitmap")

   /* Example of convert drawable to bitmap asynchronously */
   convert.drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!, {
       Log.e(TAG, "bitmap: $bitmap")
   },{
       Log.e(TAG, "ex: $ex")
   })
```

#### Convert Drawable to Base64 <a name="convert_drawable_to_base64"></a>
```kotlin
  /**
   * convert drawable to base64
   * @param drawable is the image in drawable.
   */
   
   /* Example of convert drawable to base64 synchronously */
   val base642 = convert.drawableToBase64(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!)
        Log.e(TAG, "base64: $base642")

   /* Example of convert drawable to base64 asynchronously */
   convert.drawableToBase64(ContextCompat.getDrawable(this, R.drawable.ic_android_24dp)!!, {
       Log.e(TAG, "base64: $base64")
   },{
       Log.e(TAG, "ex: $ex")
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
      .save(base64, 100, {
        //success
      },{
        //failed
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
      .savePublic(base64, 100, {
        //success
      },{
        //failed
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
      .load({
         Log.d(TAG, "Success load image test")
      },{
         Log.d(TAG, "Failed load image")
      })
  
  /* Example of load public storage synchronously */
  val bitmap = manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .loadPublic()
  
  /* Example of load public storage asynchronously */
  manage(this)
      .imageAttribute("test_public","folder/subfolder/", Extension.PNG)
      .loadPublic({
          Log.d(TAG, "Success load image test_public")
      },{       
          Log.d(TAG, "Failed load image")
      })
```
#### Delete function <a name="delete"></a>
```kotlin

  /* Example of delete internal storage synchronously */ 
  val isDeleted = manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .delete()
      
  /* Example of delete internal storage asynchronously */ 
  manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .delete({
          Log.d(TAG, "Success delete image test")
      },{
          Log.d(TAG, "Failed delete image")
      })
    
  /* Example of delete public storage synchronously */ 
  val isDeleted = manage(this)
      .imageAttribute(imageFile, imageDir, Extension.JPG)
      .deletePublic(intentSenderRequest)
      
  /* Example of delete public storage asynchronously */ 
  manage(this)
      .imageAttribute("test","folder/subfolder/", Extension.PNG)
      .deletePublic({
          Log.d(TAG, "Success delete image test")
      },{
          Log.d(TAG, "Failed delete image")
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
