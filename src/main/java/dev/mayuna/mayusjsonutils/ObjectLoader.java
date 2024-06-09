package dev.mayuna.mayusjsonutils;

import com.google.gson.Gson;
import lombok.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple utility class that allows you to load object from the specified path and save it
 */
public final class ObjectLoader {

    private ObjectLoader() {
    }

    /**
     * Loads the object specified by {@link Class} from the specified {@link Path} using the specified {@link Gson} and {@link Charset}<br>
     * The specified {@link Class} must have public no-args constructor<br>
     * If it does not exist, creates the file specified by {@link Path} and saves it.
     *
     * @param gson    Non-null {@link Gson}
     * @param path    Non-null {@link Path}
     * @param clazz   Non-null {@link Class} with the object you want to load
     * @param charset Non-null {@link Charset}
     * @param <T>     Your object type, specified in the {@link Class}
     *
     * @return Non-null object, loaded from the specified {@link Path}
     *
     * @throws IOException If I/O exception occurs (failed to write or read the file)
     */
    public static <T> T loadOrCreateFrom(@NonNull Class<T> clazz, @NonNull Path path, @NonNull Charset charset, @NonNull Gson gson) throws IOException {
        if (!Files.exists(path)) {
            try {
                MayuJson.saveJson(gson.toJsonTree(clazz.getConstructor().newInstance()).getAsJsonObject(), path, charset, gson);
            } catch (Exception exception) {
                throw new IOException("Could not save default object " + clazz.getName() + " at " + path.toAbsolutePath() + "!", exception);
            }
        }

        T object;

        try {
            object = gson.fromJson(MayuJson.createOrLoadJsonObject(path).getJsonObject(), clazz);
        } catch (Exception exception) {
            throw new IOException("Could not load object " + clazz.getName() + " at " + path.toAbsolutePath() + "! Please, check if there are no errors in JSON.", exception);
        }

        try {
            MayuJson.saveJson(gson.toJsonTree(object).getAsJsonObject(), path, charset, gson);
        } catch (Exception exception) {
            throw new IOException("Could not save loaded object " + clazz.getName() + " at " + path.toAbsolutePath() + "!", exception);
        }

        return object;
    }

    /**
     * Loads the object specified by {@link Class} from the specified {@link Path} using the {@link MayuJson#DEFAULT_GSON} and specified
     * {@link Charset}<br>
     * The specified {@link Class} must have public no-args constructor<br>
     * If it does not exist, creates the file specified by {@link Path} and saves it.
     *
     * @param path  Non-null {@link Path}
     * @param clazz Non-null {@link Class} with the object you want to load
     * @param <T>   Your object type, specified in the {@link Class}
     *
     * @return Non-null object, loaded from the specified {@link Path}
     *
     * @throws IOException If I/O exception occurs (failed to write or read the file)
     */
    public static <T> T loadOrCreateFrom(@NonNull Class<T> clazz, @NonNull Path path, @NonNull Charset charset) throws IOException {
        return loadOrCreateFrom(clazz, path, charset, MayuJson.DEFAULT_GSON);
    }

    /**
     * Loads the object specified by {@link Class} from the specified {@link Path} using the specified {@link Gson} and
     * {@link java.nio.charset.StandardCharsets#UTF_8}<br>
     * The specified {@link Class} must have public no-args constructor<br>
     * If it does not exist, creates the file specified by {@link Path} and saves it.
     *
     * @param gson  Non-null {@link Gson}
     * @param path  Non-null {@link Path}
     * @param clazz Non-null {@link Class} with the object you want to load
     * @param <T>   Your object type, specified in the {@link Class}
     *
     * @return Non-null object, loaded from the specified {@link Path}
     *
     * @throws IOException If I/O exception occurs (failed to write or read the file)
     */
    public static <T> T loadOrCreateFrom(@NonNull Class<T> clazz, @NonNull Path path, @NonNull Gson gson) throws IOException {
        return loadOrCreateFrom(clazz, path, StandardCharsets.UTF_8, gson);
    }

    /**
     * Loads the object specified by {@link Class} from the specified {@link Path} using the {@link MayuJson#DEFAULT_GSON} and
     * {@link java.nio.charset.StandardCharsets#UTF_8}<br>
     * The specified {@link Class} must have public no-args constructor<br>
     * If it does not exist, creates the file specified by {@link Path} and saves it.
     *
     * @param path  Non-null {@link Path}
     * @param clazz Non-null {@link Class} with the object you want to load
     * @param <T>   Your object type, specified in the {@link Class}
     *
     * @return Non-null object, loaded from the specified {@link Path}
     *
     * @throws IOException If I/O exception occurs (failed to write or read the file)
     */
    public static <T> T loadOrCreateFrom(@NonNull Class<T> clazz, @NonNull Path path) throws IOException {
        return loadOrCreateFrom(clazz, path, StandardCharsets.UTF_8, MayuJson.DEFAULT_GSON);
    }

    /**
     * Saves the specified object to the specified {@link Path} using the specified {@link Gson} and {@link Charset}
     *
     * @param gson    Gson
     * @param path    Path
     * @param object  Object
     * @param charset Non-null {@link Charset}
     */
    public static void saveTo(@NonNull Object object, @NonNull Path path, @NonNull Charset charset, @NonNull Gson gson) throws IOException {
        try {
            MayuJson.saveJson(gson.toJsonTree(object).getAsJsonObject(), path, charset, gson);
        } catch (Exception exception) {
            throw new IOException("Could not save object " + object.getClass().getName() + " at " + path.toAbsolutePath() + "!", exception);
        }
    }

    /**
     * Saves the specified object to the specified {@link Path} using {@link MayuJson#DEFAULT_GSON} and specified {@link Charset}
     *
     * @param path    Path
     * @param object  Object
     * @param charset Non-null {@link Charset}
     */
    public static void saveTo(@NonNull Object object, @NonNull Path path, @NonNull Charset charset) throws IOException {
        saveTo(object, path, charset, MayuJson.DEFAULT_GSON);
    }

    /**
     * Saves the specified object to the specified {@link Path} using the specified {@link Gson} and {@link StandardCharsets#UTF_8}
     *
     * @param gson   Gson
     * @param path   Path
     * @param object Object
     */
    public static void saveTo(@NonNull Object object, @NonNull Path path, @NonNull Gson gson) throws IOException {
        saveTo(object, path, StandardCharsets.UTF_8, gson);
    }

    /**
     * Saves the specified object to the specified {@link Path} using {@link MayuJson#DEFAULT_GSON} and {@link StandardCharsets#UTF_8}
     *
     * @param path   Path
     * @param object Object
     */
    public static void saveTo(@NonNull Object object, @NonNull Path path) throws IOException {
        saveTo(object, path, StandardCharsets.UTF_8, MayuJson.DEFAULT_GSON);
    }
}
