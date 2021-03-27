package lilmayu.mayusjsonutils.objects;

import com.google.gson.*;
import lilmayu.mayusjsonutils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MayuJson {

    private @Getter @Setter File file;
    private @Getter JsonObject jsonObject;

    public MayuJson(File file, JsonObject jsonObject) {
        this.file = file;
        this.jsonObject = jsonObject;
    }

    /**
     * Reloads JsonObject from current File.
     */
    public void reloadJson() throws IOException {
        this.jsonObject = JsonUtil.createOrLoadJsonFromFile(file).getJsonObject();
    }

    /**
     * Pretty saves current JsonObject into current File. Just a wrapper for {@link #saveJson(boolean)}, which is called with value true.
     */
    public void saveJson() throws IOException {
        saveJson(true);
    }

    /**
     * Saves current JsonObject into current File.
     *
     * @param pretty Determines if json from current JsonObject should be pretty-printed into current File.
     */
    public void saveJson(boolean pretty) throws IOException {
        Gson gson;
        if (pretty)
            gson = new GsonBuilder().setPrettyPrinting().create();
        else
            gson = new GsonBuilder().create();
        JsonElement jsonElement = new JsonParser().parse(jsonObject.toString());
        String jsonString = gson.toJson(jsonElement);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        outputStreamWriter.write(jsonString);
        outputStreamWriter.close();
    }

    /**
     * Sets current jsonObject to json in this supplied String
     * @param json Json in string
     */
    public void setJsonObject(String json) {
        this.jsonObject = new JsonParser().parse(json).getAsJsonObject();
    }

    /**
     * Sets current jsonObject
     * @param jsonObject JsonObject
     */
    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
