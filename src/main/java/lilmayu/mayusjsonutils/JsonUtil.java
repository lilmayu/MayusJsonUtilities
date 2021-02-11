package lilmayu.mayusjsonutils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.internal.Nullable;
import lilmayu.mayusjsonutils.objects.MayuJson;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JsonUtil class - Main entry-point for Mayu's Json Utilities
 *
 * Made by: lilmayu
 * Date: 11.02.2021
 */
public class JsonUtil {

    /**
     * Creates empty json file if specified path does not exist. If it exists, loads current data from file.
     *
     * @param path Path where should be created / loaded json file
     * @return Returns {@link MayuJson}
     * @throws IOException Throws {@link IOException} when it failed to create required directories / failed to read/write from/to file.
     */
    public static MayuJson createOrLoadJsonFromFile(@NonNull String path) throws IOException {
        return createOrLoadJsonFromFile(new File(FilenameUtils.getPath(path)));
    }

    /**
     * Creates empty json file if specified path does not exist. If it exists, loads current data from file.
     *
     * @param path Path where should be created / loaded json file
     * @return Returns {@link MayuJson}
     * @throws IOException Throws {@link IOException} when it failed to create required directories / failed to read/write from/to file.
     */
    public static MayuJson createOrLoadJsonFromFile(@NonNull Path path) throws IOException {
        return createOrLoadJsonFromFile(path.toFile());
    }

    /**
     * Creates empty json file if specified path does not exist. If it exists, loads current data from file.
     *
     * @param file File where should be created / loaded json file
     * @return Returns {@link MayuJson}
     * @throws IOException Throws {@link IOException} when it failed to create required directories / failed to read/write from/to file.
     */
    public static MayuJson createOrLoadJsonFromFile(@NonNull File file) throws IOException {
        file.mkdirs();
        if (!Files.exists(file.toPath())) {
            throw new IOException("Failed to create directories for specified path! Path: " + file.toPath().toAbsolutePath());
        }

        if (!file.exists()) {
            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write("{}");
            fileWriter.close();
        }

        return loadJson(file.getPath());
    }

    /**
     * Loads json file in location of specified path. Does not create new file if file does not exist.
     * If you wanted to create/load file, see {@link #createOrLoadJsonFromFile(String)}
     *
     * @param path Path where should be json file
     * @return Returns null, when specified json file does not exist, otherwise returns {@link MayuJson}
     * @throws IOException Throws {@link IOException} when it failed to read from file
     */
    public static @Nullable MayuJson loadJson(@NonNull String path) throws IOException {
        Path pathObject = Paths.get(path);

        if (!Files.exists(pathObject)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String line : Files.readAllLines(pathObject)) {
            stringBuilder.append(line);
        }
        JsonObject jsonObject = JsonParser.parseString(stringBuilder.toString()).getAsJsonObject();
        return new MayuJson(pathObject.toFile(), jsonObject);
    }

    /**
     * Saves specified json to specified file
     *
     * @param json Valid json string
     * @param file File to save json to
     * @return {@link MayuJson} if needed
     * @throws IOException Throws {@link IOException} when it failed to save specified json into specified file
     */
    public static MayuJson saveJson(@NonNull String json, @NonNull File file) throws IOException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return saveJson(jsonObject, file, true);
    }

    /**
     * Saves specified json to specified file
     *
     * @param jsonObject Valid {@link JsonObject}
     * @param file File to save json to
     * @return {@link MayuJson} if needed
     * @throws IOException Throws {@link IOException} when it failed to save specified json into specified file
     */
    public static MayuJson saveJson(@NonNull JsonObject jsonObject, File file) throws IOException {
        return saveJson(jsonObject, file, true);
    }

    /**
     * Saves specified json to specified file
     *
     * @param jsonObject Valid {@link JsonObject}
     * @param file File to save json to
     * @param pretty If json should be pretty-printed into specified file
     * @return {@link MayuJson} if needed
     * @throws IOException Throws {@link IOException} when it failed to save specified json into specified file
     */
    public static MayuJson saveJson(@NonNull JsonObject jsonObject, @NonNull File file, boolean pretty) throws IOException {
        MayuJson mayuJson = new MayuJson(file, jsonObject);
        mayuJson.saveJson(pretty);
        return mayuJson;
    }
}
