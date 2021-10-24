package dev.mayuna.mayusjsonutils.objects;

import com.google.gson.*;
import dev.mayuna.mayusjsonutils.JsonUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MayuJson {

    private @Getter @Setter File file;
    private @Getter JsonObject jsonObject;

    public MayuJson(@NonNull JsonObject jsonObject) {
        this.file = null;
        this.jsonObject = jsonObject;
    }

    public MayuJson(@NonNull File file, @NonNull JsonObject jsonObject) {
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
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

        if (pretty)
            gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        outputStreamWriter.write(gson.toJson(jsonObject));
        outputStreamWriter.close();
    }

    /**
     * Gets Member by passed parameter
     *
     * @param memberName Member name in JSON file
     *
     * @return If member exists, it returns it's {@link JsonElement}, otherwise returns null
     */
    public JsonElement getOrNull(String memberName) {
        if (jsonObject.has(memberName)) {
            JsonElement jsonElement = jsonObject.get(memberName);

            if (!jsonElement.isJsonNull()) {
                return jsonElement;
            }

        }

        return null;
    }

    /**
     * Gets Member by passed parameter, if it does not exists, it adds default value to loaded JSON
     *
     * @param memberName   Member name in JSON file
     * @param defaultValue Default value to set it, if member does not exists
     *
     * @return If member exists, it returns it's {@link JsonElement}, otherwise it adds to loaded JSON default value and returns it
     */
    public JsonElement getOrCreate(String memberName, JsonElement defaultValue) {
        if (jsonObject.has(memberName)) {
            JsonElement jsonElement = jsonObject.get(memberName);

            if (!jsonElement.isJsonNull()) {
                return jsonElement;
            }
        }

        jsonObject.add(memberName, defaultValue);
        return defaultValue;
    }

    /**
     * Adds {@link JsonElement} to current {@link JsonObject}
     *
     * @param memberName Member name
     * @param value      Json Element
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(String memberName, JsonElement value) {
        jsonObject.add(memberName, value);
        return this;
    }

    /**
     * Adds {@link Number} to current {@link JsonObject}
     *
     * @param memberName Member name
     * @param value      Number
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(String memberName, Number value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link String} to current {@link JsonObject}
     *
     * @param memberName Member name
     * @param value      String
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(String memberName, String value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link Boolean} to current {@link JsonObject}
     *
     * @param memberName Member name
     * @param value      Boolean
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(String memberName, Boolean value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link Character} to current {@link JsonObject}
     *
     * @param memberName Member name
     * @param value      Character
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(String memberName, Character value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Removes member from current {@link JsonObject}
     *
     * @param memberName Member name
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson remove(String memberName) {
        jsonObject.remove(memberName);
        return this;
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object. (from {@link JsonObject#has(String)}
     *
     * @param memberName Member name
     *
     * @return true if there is a member with the specified name, false otherwise. (from {@link JsonObject#has(String)}
     */
    public boolean has(String memberName) {
        return jsonObject.has(memberName);
    }

    /**
     * Sets current jsonObject to json in this supplied String
     *
     * @param json Json in string
     */
    public void setJsonObject(String json) {
        this.jsonObject = new JsonParser().parse(json).getAsJsonObject();
    }

    /**
     * Sets current jsonObject
     *
     * @param jsonObject JsonObject
     */
    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
