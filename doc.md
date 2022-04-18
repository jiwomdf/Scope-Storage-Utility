#### Convert Bitmap to Base64
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
