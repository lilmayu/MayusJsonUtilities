package lilmayu.mayusjsonutils.objects;

import com.google.gson.*;
import lilmayu.mayusjsonutils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MayuJson {

    private @Getter @Setter File file;
    private @Getter @Setter JsonObject jsonObject;

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
     * @param pretty Determines if json from current JsonObject should be pretty-printed into current File.
     */
    public void saveJson(boolean pretty) throws IOException {
        Gson gson;
        if (pretty)
            gson = new GsonBuilder().setPrettyPrinting().create();
        else
            gson = new GsonBuilder().create();
        JsonElement jsonElement = JsonParser.parseString(jsonObject.toString());
        String jsonString = gson.toJson(jsonElement);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonString);
        fileWriter.close();
    }
}