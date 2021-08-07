# EasyImageLibrary


### Convert Image 
```
  val base64 = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG)
  val base64Async = convert.bitmapToBase64(captureImage, 100, Bitmap.CompressFormat.PNG,
      object : IConvertBitmap.Base64CallBack {
          override fun onResult(base64: String) {

          }

          override fun onFailed(err: String) {

          }
  })
```

### Manage image 
```
  manage(applicationContext)
      .imageAttribute("test", "myfolder", Extension.PNG)
      .save(base64!!,100)
```
