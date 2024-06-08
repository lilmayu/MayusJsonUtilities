package dev.mayuna.mayusjsonutils;

import com.google.gson.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main entrypoint for Mayu's JSON Utilities<br>
 * Handles all tedious actions such as loading and saving JSON. Also does have some utility methods to ease your day when working with JsonObjects<br>
 * To load JSON from filesystem, see {@link #createOrLoadJsonObject(Path, Charset)}
 */
@SuppressWarnings("unused")
@Getter
public final class MayuJson {

    /**
     * Default GSON instance which is used by {@link MayuJson}. Serializes nulls and enables pretty printing :3
     */
    public static final Gson DEFAULT_GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    public static final String EMPTY_JSON_OBJECT = "{}";

    private @Setter @NonNull Path path;
    private @Setter @NonNull Charset charset;
    private JsonObject jsonObject;

    /**
     * Constructs {@link MayuJson} with specified {@link Path}, {@link Charset} and {@link JsonObject}
     *
     * @param path       Non-null {@link Path}
     * @param charset    Non-null {@link Charset}
     * @param jsonObject Non-null {@link JsonObject}
     */
    public MayuJson(@NonNull Path path, @NonNull Charset charset, @NonNull JsonObject jsonObject) {
        this.path = path;
        this.charset = charset;
        this.jsonObject = jsonObject;
    }

    /**
     * Constructs {@link MayuJson} with specified {@link Path} and {@link JsonObject}. Uses {@link StandardCharsets#UTF_8} as charset<br>
     * Internally invokes {@link MayuJson#MayuJson(Path, Charset, JsonObject)}
     *
     * @param path       Non-null {@link Path}
     * @param jsonObject Non-null {@link JsonObject}
     */
    public MayuJson(@NonNull Path path, @NonNull JsonObject jsonObject) {
        this(path, StandardCharsets.UTF_8, jsonObject);
    }

    /**
     * Creates or loads {@link MayuJson} from specified {@link Path}. Also creates any missing directories leading to the specified {@link Path}<br>
     * Internally invokes {@link #loadJsonObject(Path, Charset)} to load the JSON
     *
     * @param path    Non-null {@link Path}
     * @param charset Non-null {@link Charset}
     *
     * @return Non-null {@link MayuJson}
     *
     * @throws IOException When I/O exception occurs (unable to create directors or unable to read file)
     */
    public static MayuJson createOrLoadJsonObject(@NonNull Path path, @NonNull Charset charset) throws IOException {
        if (!Files.exists(path)) {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Files.writeString(path, EMPTY_JSON_OBJECT);
        }

        return loadJsonObject(path, charset);
    }

    /**
     * Creates or loads {@link MayuJson} from specified {@link Path}. Also creates any missing directories leading to the specified {@link Path}<br>
     * Internally invokes {@link #loadJsonObject(Path, Charset)} to load the JSON with {@link StandardCharsets#UTF_8}
     *
     * @param path Non-null {@link Path}
     *
     * @return Non-null {@link MayuJson}
     *
     * @throws IOException When I/O exception occurs (unable to create directors or unable to read file)
     */
    public static MayuJson createOrLoadJsonObject(@NonNull Path path) throws IOException {
        return createOrLoadJsonObject(path, StandardCharsets.UTF_8);
    }

    /**
     * Loads {@link  MayuJson} from specified {@link Path}. Returns null when the specified file in {@link Path} does not exist.<br>
     * If the loaded JSON is empty, loads it with {@link #EMPTY_JSON_OBJECT}
     *
     * @param path    Non-null {@link Path}
     * @param charset Non-null {@link Charset}
     *
     * @return Nullable {@link MayuJson} (null when the specified file in {@link Path} does not exist)
     *
     * @throws IOException When I/O exception occurs (unable to read file)
     */
    public static MayuJson loadJsonObject(@NonNull Path path, @NonNull Charset charset) throws IOException {
        if (!Files.exists(path)) {
            return null;
        }

        String json = Files.readString(path, charset);

        if (json.isBlank()) {
            json = EMPTY_JSON_OBJECT;
        }

        return new MayuJson(path, charset, JsonParser.parseString(json).getAsJsonObject());
    }

    /**
     * Loads {@link  MayuJson} from specified {@link Path}. Returns null when the specified file in {@link Path} does not exist.<br>
     * If the loaded JSON is empty, loads it with {@link #EMPTY_JSON_OBJECT}<br>
     * Invokes {@link #loadJsonObject(Path, Charset)} with specified {@link Path} and {@link StandardCharsets#UTF_8}
     *
     * @param path Non-null {@link Path}
     *
     * @return Nullable {@link MayuJson} (null when the specified file in {@link Path} does not exist)
     *
     * @throws IOException When I/O exception occurs (unable to read file)
     */
    public static MayuJson loadJsonObject(@NonNull Path path) throws IOException {
        return loadJsonObject(path, StandardCharsets.UTF_8);
    }

    /**
     * Reloads current {@link MayuJson} from the filesystem<br>
     * Internally invokes {@link MayuJson#createOrLoadJsonObject(Path, Charset)} and sets the result of {@link #getJsonObject()} to the current
     * {@link JsonObject}
     */
    public void reload() throws IOException {
        this.jsonObject = MayuJson.createOrLoadJsonObject(path, charset).getJsonObject();
    }

    /**
     * Saves current {@link MayuJson} to the filesystem to the current {@link Path} with current {@link Charset}
     *
     * @param gson Non-null {@link Gson} to use when converting {@link JsonObject} into {@link String}
     *
     * @throws IOException When I/O exception occurs while saving file
     */
    public void save(Gson gson) throws IOException {
        Files.writeString(path, gson.toJson(jsonObject), charset);
    }

    /**
     * Saves current {@link MayuJson} to the filesystem to the current {@link Path} with current {@link Charset}<br>
     * Uses {@link #DEFAULT_GSON}
     *
     * @throws IOException When I/O exception occurs while saving file
     */
    public void save() throws IOException {
        save(DEFAULT_GSON);
    }

    /**
     * Sets current {@link JsonObject} to JSON in this supplied String
     *
     * @param json Non-null JSON in string
     */
    public void setJsonObject(@NonNull String json) {
        this.jsonObject = JsonParser.parseString(json).getAsJsonObject();
    }

    /**
     * Sets current {@link JsonObject}
     *
     * @param jsonObject Non-null {@link JsonObject}
     */
    public void setJsonObject(@NonNull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Returns {@link JsonElement} with specified name
     *
     * @param memberName Non-null member name
     *
     * @return Nullable {@link JsonElement} (null when the current {@link JsonObject} does not have such specified member or the member is {@link JsonNull})
     */
    public JsonElement getOrNull(@NonNull String memberName) {
        if (jsonObject.has(memberName)) {
            JsonElement jsonElement = jsonObject.get(memberName);

            if (!jsonElement.isJsonNull()) {
                return jsonElement;
            }

        }

        return null;
    }

    /**
     * Returns {@link JsonElement} with specified name if exists. Otherwise, adds the specified value to the current {@link JsonObject} and returns the
     * same specified value.
     *
     * @param memberName   Non-null member name
     * @param defaultValue Non-null {@link JsonElement}
     *
     * @return Non-null {@link JsonElement} (returns the same instance of specified <code>defaultValue</code> if specified member does not exist)
     */
    public JsonElement getOrCreate(@NonNull String memberName, @NonNull JsonElement defaultValue) {
        JsonElement existingElement = getOrNull(memberName);

        if (existingElement != null) {
            return existingElement;
        }

        add(memberName, defaultValue);
        return defaultValue;
    }

    /**
     * Adds {@link JsonElement} to current {@link JsonObject}
     *
     * @param memberName Non-null member name
     * @param value      Non-null {@link JsonElement}
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson add(@NonNull String memberName, @NonNull JsonElement value) {
        jsonObject.add(memberName, value);
        return this;
    }

    /**
     * Adds {@link Number} to current {@link JsonObject}
     *
     * @param memberName Non-null member name
     * @param value      Non-null {@link Number}
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson addProperty(@NonNull String memberName, @NonNull Number value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link String} to current {@link JsonObject}
     *
     * @param memberName Non-null member name
     * @param value      Non-null {@link String}
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson addProperty(@NonNull String memberName, @NonNull String value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link Boolean} to current {@link JsonObject}
     *
     * @param memberName Non-null member name
     * @param value      Non-null {@link Boolean}
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson addProperty(@NonNull String memberName, @NonNull Boolean value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Adds {@link Character} to current {@link JsonObject}
     *
     * @param memberName Non-null member name
     * @param value      Non-null {@link Character}
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson addProperty(@NonNull String memberName, @NonNull Character value) {
        jsonObject.addProperty(memberName, value);
        return this;
    }

    /**
     * Removes member from current {@link JsonObject}
     *
     * @param memberName Non-null member name
     *
     * @return Returns itself, great for chaining
     */
    public MayuJson remove(@NonNull String memberName) {
        jsonObject.remove(memberName);
        return this;
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object. (from {@link JsonObject#has(String)}
     *
     * @param memberName Non-null member name
     *
     * @return true if there is a member with the specified name, false otherwise. (from {@link JsonObject#has(String)}
     */
    public boolean has(@NonNull String memberName) {
        return jsonObject.has(memberName);
    }

    /**
     * Determines if specified member is {@link JsonNull}
     *
     * @param memberName Non-null member name
     *
     * @return <code>true</code> the specified member is {@link JsonNull} <code>false</code> otherwise (<strong><code>false</code> also when the specified member does not exist</strong>)
     */
    public boolean isJsonNull(@NonNull String memberName) {
        return jsonObject.get(memberName).isJsonNull();
    }
}
