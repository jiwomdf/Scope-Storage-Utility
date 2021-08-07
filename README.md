# EasyImageLibrary
Easy Image Library is an image library to help you converting and managing your image that easy to use. <br>
it also provide an asynchronous solution. <br> 
it also safe from throwing an error. <br>

1. Convert image <br>
  a. convert bitmap to base64 <br>
  b. convert base64 to Bitmap <br>
  c. convert base64 to Drawable <br>
  d. convert drawable to Bitmap <br>
  e. convert drawable to base64 <br> <br>
  
2. Manage image <br>
  a. load image <br>
  b. delete image <br>
  c. save bitmap formatted image <br>
  d. save base64 formatted image <br>
  e. save drawable formatted image <br>
  f. save drawable formatted image <br> <br>
  

### Convert Image 
```
  val base64 = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG) /* synchronously */
  
  convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG, /* asynchronously */
      object : IConvertImage.Base64CallBack {
          override fun onResult(base64: String) {

          }

          override fun onFailed(err: String) {

          }
  })
```

### Manage image 
```

  manage(applicationContext) /* synchronously */
      .imageAttribute("test", "myfolder", Extension.PNG)
      .save(base64!!,100)
      
  manage(applicationContext) /* asynchronously */
      .imageAttribute("test", "myfolder", Extension.PNG)
      .save(base64, 100, object : IManageImage.SaveBase64CallBack {
          override fun onSuccess() {

          }
          override fun onFailed(err: String) {

          }
      })
```
