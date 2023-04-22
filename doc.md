# Scope Storage Utility Docs

## Table of Contents
1. [Manage Image Storage](#manage) <br>
   a. [Save Private And Shared Image](#save) <br>
   b. [Load Private And Shared Image](#load) <br>
   c. [Delete Private And Shared Image](#delete) <br>
   d. [Load Image Uri of Shared Storage](#loaduri)   
   e. [Load Outstream For File Creation in Private And Shared](#load) <br>

Before starting with the lib, here are some useful information
```
1. For syncronize funtion, it will always returned true if the process is success and false if it failed
2. For tracing the error you can try by searching the logcat of "ScopeStorageUtility"
3. For better understanding about this lib, you can check / clone this repository and see the MainActivity file
```

## Manage Image Storage <a name="manage"></a>
#### Manage, isShareStorage, attribute function
```kotlin

  manage() -> 
  /**
   * @param context, the activity or app context
   */

  isShareStorage() -> 
  /**
   *  @param value, the value of isShareStorage, 
   *  if it's false then it mean it will be save private storage, you can find in your_phone_name\Android\data\application_package_name
   *  if it's true then it mean it will be save to shared storage
   */
   //if the isShareStorage is not inputed, it will be false as it default value
   
  attribute() -> 
  /**
   *  @param fileName is the name of your file.
   *  @param directory is the directory of the file you want to save or load,
   *  @param env is the Environment of the file you want to save or load, eg: Environment.DIRECTORY_DCIM
   *  @param extension is the file extension, it can be ".png", ".jpeg", ".jpg", ".webp".
   *  you can use eg: Extension.get(Extension.PNG) or create new extension eg: Extension.ExtensionModel(".png", "image/png")
   */   
```

#### Save function <a name="save"></a>
```kotlin
  /***
   * Example of saving bitmap to private storage 
   * in your_phone_name\Android\data\application_package_name\DCIM\folder\subfolder\test_shared.png
   */
  /* Example of save private storage asynchronously */
  val isSuccess = manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG),
      )
      .save(base64, 100)
      
  /* Example of save private storage asynchronously */
  manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG),
      )
      .save(base64, 100, {
        //success
      },{
        //failed
      })
      
      
  /***
   * Example of save shared storage in DCIM folder
   */
  
  /* Example of save shared storage synchronously */
  manage(this)
      isShareStorage(true)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG),
      )
      .save(base64, 100)
  
  /* Example of save shared storage asynchronously */
  manage(this)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG),
      )
      .save(base64, 100, {
        //success
      },{
        //failed
      })
```

#### Load function <a name="load"></a>
```kotlin
  /* Example of load private storage synchronously */
  val bitmap = manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .load()
            
  /* Example of load private storage asynchronously */
  manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .load({
        //success
      },{
        //failed
      })
  
  /* Example of load shared storage synchronously */
  val bitmap = manage(this)
      .isShareStorage(true)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .load()
  
  /* Example of load shared storage asynchronously */
  manage(this)
      .isShareStorage(true)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .load({
        //success
      },{
        //failed
      })
```
#### Delete function <a name="delete"></a>
```kotlin

  /* Example of delete private storage synchronously */ 
  val isDeleted = manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .delete(null)
      
  /* Example of delete private storage asynchronously */ 
  manage(this)
      .isShareStorage(false)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .delete(null, {
        //success
      },{       
        //failed
      })
    
  /* Example of delete shared storage synchronously */ 
  val isDeleted = manage(this)
      .isShareStorage(true)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .delete(intentSenderRequest)
      
  /* Example of delete shared storage asynchronously */ 
  manage(this)
      .isShareStorage(true)
      .attribute(
          fileName = "test_shared",
          directory = "folder/subfolder/",
          env = Environment.DIRECTORY_DCIM,
          extension = Extension.get(Extension.PNG)
      )
      .delete(intentSenderRequest, {
          //success
      },{
          //failed
      })
      
  /**
    * for delete shared image you need intentSenderRequest
    */
    intentSenderRequest = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
          if (it.resultCode == RESULT_OK) {
               //user confirm other app file deletion
          } else {
               //user reject other app file deletion
          }
      }
```

#### Load File URI (for shared storage) <a name="loaduri"></a>
```kotlin
    /***
     * Example getting the file URI of shared file
     */
      manage(this)
         .isShareStorage(true)
         .attribute(
             fileName = "test_shared",
             directory = "folder/subfolder/",
             env = Environment.DIRECTORY_DCIM,
             extension = Extension.get(Extension.PNG)
         )
         .loadSharedFileUri(this, BuildConfig.APPLICATION_ID)
         .also {
             showToast("uri: $it")
         }
```

#### Create File from outputStream <a name="loaduri"></a>
```kotlin
    /***
     * Example of creating .txt file from the outputStream
     */
      
      manage(this)
            .isShareStorage(true)
            .attribute(
                fileName = filename,
                directory = fileDir,
                env = Environment.DIRECTORY_DOWNLOADS,
                extension = Extension.ExtensionModel(".txt", "text/plain"),
            )
            .getOutputStream({
                val outWriter = OutputStreamWriter(it)
                outWriter.append("this is shared storage text")
                outWriter.close()
                it.close()

                //Success write txt file on DCIM/fileDir/filename in shared storage
            }, {
                //Failed write txt file in share storage
            })
```
