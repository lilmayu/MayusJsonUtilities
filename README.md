# Mayu's Json Utilities
## Usage
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
## Gradle
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.lilmayu:Mayu-s-Json-Utilities:main-SNAPSHOT' // Always newest build
}
```
## Maven
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.lilmayu</groupId>
        <artifactId>Mayu-s-Json-Utilities</artifactId>
    <version>main-SNAPSHOT</version>
</dependency>
```
For more info see [JitPack](https://jitpack.io/#lilmayu/Mayu-s-Json-Utilities/main-SNAPSHOT)

This library uses:
 - Gson
 - Lombok
 - Commons-io

TODO: Wiki