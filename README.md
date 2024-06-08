# Mayu's JSON Utilities

Simple JSON Utilities which I use in my projects

> [!IMPORTANT]  
> As of 2.0, the library is now under MIT license. All previous versions are under LGPL 3.0.

## Contents

- [Features](#features)
- [Installation](#installation)
  - [Gradle](#gradle)
  - [Maven](#maven)
- [Documentation](#documentation)

## Features

## Installation

### Gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'dev.mayuna:mayus-json-utilities:2.0'
  
    // Required
    implementation 'com.google.code.gson:gson:2.11.0'
}
```

### Maven

```xml
<dependency>
    <groupId>dev.mayuna</groupId>
    <artifactId>mayus-json-utilities</artifactId>
    <version>2.0</version>
</dependency>

<!-- Required -->
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.11.0</version>
</dependency>
```

**You can find the latest version [here](https://mvnrepository.com/artifact/dev.mayuna/mayus-library).**

## Documentation
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