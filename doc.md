# Scope Storage Utility Docs

## Table of Contents
   1. [Manage, isShareStorage, attribute functions](#manage) <br>
   2. [Save Image in Private And Shared Storage](#save) <br>
   3. [Load Image in Private And Shared Storage](#load) <br>
   4. [Delete Image in Private And Shared Storage](#delete) <br>
   5. [Load Image/File Uri From Shared Storage](#loaduri) <br>
   6. [Load Outstream For File Creation in Private And Shared Storage](#outstream) <br>
   7. [Delete File in Private And Shared Storage](#filedelete) 
   
<br>

Before starting with the lib, here are some useful information
```
1. For tracing the error you can try by searching the logcat of "ScopeStorageUtility"
2. For better understanding about this lib, you can check / clone this repository and see the MainActivity file
```

## Manage Image Storage <a name="manage"></a>
#### Manage, isShareStorage, attribute function
```kotlin

  manage(context: Context)
  /**
   * @param context, the activity or app context
   */

  isShareStorage(value: Boolean)
  /**
   *  @param value, the value of isShareStorage, 
   *  if it's false then it mean it will be save private storage, you can find in your_phone_name\Android\data\application_package_name
   *  if it's true then it mean it will be save to shared storage
   *  if it's not inputed, it will be false as it's the default value
   */
   
  attribute(fileName: String, directory: String?, env: String, extension: Extension.ExtensionModel)
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
  /**
   * Example of saving bitmap to private storage 
   * in your_phone_name\Android\data\application_package_name\DCIM\folder\subfolder\test_shared.png
   */

  /* Example of save asynchronously */
  val isSuccess = manage(this)
      .isShareStorage(false)
      .attribute(
         UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .save(base64, 100)
      
  /* Example of save asynchronously */
  manage(this)
      .isShareStorage(false)
      .attribute(
         UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .save(base64, 100, {
        //success
      },{
        //failed
      })
```

#### Load function <a name="load"></a>
```kotlin
  /* Example of load synchronously */
  val bitmap = manage(this)
      .isShareStorage(false)
      .attribute(
          UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .load()
            
  /* Example of load asynchronously */
  manage(this)
      .isShareStorage(false)
      .attribute(
         UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
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
         UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .delete(null)
      
  /* Example of delete private storage asynchronously */ 
  manage(this)
      .isShareStorage(false)
      .attribute(
         UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
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
          UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .delete(intentSenderRequest)
      
  /* Example of delete shared storage asynchronously */ 
  manage(this)
      .isShareStorage(true)
      .attribute(
          UtilityModel(
            fileName = "test",
            directory = "folder/subfolder/",
            env = Environment.DIRECTORY_DCIM,
            extension = Extension.get(Extension.PNG)
         )
      )
      .delete(intentSenderRequest, {
          //success
      },{
          //failed
      })
      
   /* for delete shared image you need intentSenderRequest */
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
    /* Example getting the file URI of shared file */
      manage(this)
         .isShareStorage(true)
         .attribute(
            UtilityModel(
            	fileName = "test",
            	directory = "folder/subfolder/",
            	env = Environment.DIRECTORY_DCIM,
            	extension = Extension.get(Extension.PNG)
            )
         )
         .loadSharedFileUri(this, BuildConfig.APPLICATION_ID)
         .also {
             showToast("uri: $it")
         }
```

#### Create File from outputStream <a name="outstream"></a>
```kotlin
    /* Example of creating .txt file from the outputStream */
      manage(this)
            .isShareStorage(true)
            .attribute(
               UtilityModel(
               	fileName = "test",
               	directory = "folder/subfolder/",
               	env = Environment.DIRECTORY_DCIM,
               	extension = Extension.get(Extension.PNG)
               )
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

#### Delete File <a name="filedelete"></a>
```kotlin
    /* Example of delete file */
      manage(this)
            .isShareStorage(true)
            .attribute(
               UtilityModel(
               	fileName = "test",
               	directory = "folder/subfolder/",
               	env = Environment.DIRECTORY_DCIM,
               	extension = Extension.get(Extension.PNG)
               )
            )
            .delete(intentSenderRequest, {
                //Success delete folder/subfolder/test.txt
            },{
                //Failed delete folder/subfolder/test.txt
            })
```
