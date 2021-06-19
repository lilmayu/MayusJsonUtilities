package lilmayu.mayusjsonutils.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lilmayu.mayusjsonutils.JsonUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DataFile {

    private @Getter @Setter @NonNull String fileName;
    private @Getter @Setter MayuJson mayuJson;

    public DataFile(@NonNull String fileName) {
        this.fileName = fileName;
    }

    public DataFile(@NonNull File file) {
        this.fileName = file.getPath();
    }

    public DataFile(@NonNull Path path) {
        this.fileName = path.toFile().getPath();
    }

    /**
     * Creates/loads specified file as JSON
     * @throws IOException Throws IOException when it failed to create required directories / failed to read/write from/to file.
     */
    public void reloadDataFile() throws IOException {
        mayuJson = JsonUtil.createOrLoadJsonFromFile(fileName);
    }

    /**
     * Saves currently JSON, if it is loaded
     * @throws IOException Throws IOException when it failed to save loaded JSON to file
     */
    public void saveDataFile() throws IOException {
        if (mayuJson != null)
            mayuJson.saveJson(true);
    }

    /**
     * Simply gets loaded json
     * @return Nullable {@link MayuJson}
     */
    public JsonObject getJsonObject() {
        return mayuJson.getJsonObject();
    }

    /**
     * Gets Member by passed parameter
     * @param memberName Member name in JSON file
     * @return If member exists, it returns it's {@link JsonElement}, otherwise returns null
     */
    public JsonElement getOrNull(String memberName) {
        JsonObject jsonObject = mayuJson.getJsonObject();
        if (!jsonObject.has(memberName)) {
            return null;
        } else {
            return jsonObject.get(memberName);
        }
    }

    /**
     * Gets Member by passed parameter, if it does not exists, it adds default value to loaded JSON
     * @param memberName Member name in JSON file
     * @param defaultValue Default value to set it, if member does not exists
     * @return If member exists, it returns it's {@link JsonElement}, otherwise it adds to loaded JSON default value and returns it
     */
    public JsonElement getOrCreate(String memberName, JsonElement defaultValue) {
        JsonObject jsonObject = mayuJson.getJsonObject();
        if (!jsonObject.has(memberName)) {
            jsonObject.add(memberName, defaultValue);
            return defaultValue;
        } else {
            return jsonObject.get(memberName);
        }
    }
}
