# Mayu's Json Utilities
## Information
 - Current version: `1.1`
 - Licence: `LGPL 3.0`
 - Author: `lilmayu`
## Usage
 - For more information, please, see [Wiki](https://github.com/lilmayu/MayusJsonUtilities/wiki)
```java
// Loads / creates file with json, returns MayuJson (see Wiki for further information)
JsonUtil.createOrLoadJsonFromFile("foo.json"); // With string
JsonUtil.createOrLoadJsonFromFile("baz/folder/foo.json"); // Automatically creates all missing folders
JsonUtil.createOrLoadJsonFromFile(Paths.get("../nonExistingFolder/baz.json")); // With path
JsonUtil.createOrLoadJsonFromFile(new File("./data.json")); // With file

JsonUtil.loadJson("existingFolder/something.json") // Loads json file, if exists, returns MayuJson

JsonUtil.saveJson("{\"random\": \"data\"}", new File("data.json")); // Saves json from string to file
JsonUtil.saveJson(jsonObject, new File("fromJsonObject.json", true)); // Saves json from JsonObject to file, with pretty-printing
```
```java
// Using MayuJson object
mayuJson.saveJson(); // Saves current json in this object; can be changed with #setJsonObject(String) or #setJsonObject(JsonObject)
mayuJson.reloadJson(); // Relods json from current file in this object; can be changed with #setFile(File)
mayuJson.getJsonObject(); // Simply gets JsonObject
mayuJson.getFile(); // Simply gets File
```
## Download
- ### Gradle
  ```
  repositories {
      maven { url 'https://jitpack.io' }
  }
  
  dependencies {
      compile 'com.github.lilmayu:Mayu-s-Json-Utilities:v1.2'
      
      // Needed libraries
      // GSon
      compile group: 'com.google.code.gson', name: 'gson', version: '2.8.7'

      // Commons-io
      compile group: 'commons-io', name: 'commons-io', version: '2.11.0'
  }
  ```
- Alternatively, you can download via [Releases](https://github.com/lilmayu/MayusJsonUtilities/releases) section.

## Experimental features
- JsonMaker and JsonLoader - You should not use it in production. Actually, Gson have same feature, which is safer.

## Libraries used
This library uses:
 - Gson
 - Lombok
 - Commons-io
