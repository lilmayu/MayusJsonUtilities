package lilmayu.mayusjsonutils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lilmayu.mayusjsonutils.objects.MayuJson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {

    public static Object loadJsonObject(File file, Class<?> clazz) throws IOException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        MayuJson mayuJson = JsonUtil.createOrLoadJsonFromFile(file);
        JsonObject jsonObject = mayuJson.getJsonObject();

        return makeObjectFromJsonMaker(jsonObject, clazz);
    }

    private static Object makeObjectFromJsonMaker(JsonObject jsonObject, Class<?> clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException, ClassNotFoundException {
        Object object = clazz.newInstance();
        System.out.println("- First debug: " + object.getClass().getName());

        for (Field field : object.getClass().getDeclaredFields()) {
            System.out.println(" ^- Field name: " + field.getName());
        }

        for (String key : jsonObject.keySet()) {
            JsonElement jsonElement = jsonObject.get(key);

            if (jsonElement.isJsonPrimitive()) {
                Field field = object.getClass().getDeclaredField(getFieldNameFromKey(key));
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, getValue(getClassNameFromKey(key), jsonElement));
                field.setAccessible(wasAccessible);
            } else if (jsonElement.isJsonObject()) {
                System.out.println("Is jsonObject, name: " + getClassNameFromKey(key));
                Object anotherObject = makeObjectFromJsonMaker(jsonElement.getAsJsonObject(), Class.forName(getClassNameFromKey(key)));
                Field field = object.getClass().getDeclaredField(getFieldNameFromKey(key));
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, anotherObject);
                field.setAccessible(wasAccessible);
            } else if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                String name = getListType(key);
                List<Object> list = new ArrayList<>();

                for (JsonElement jsonElementInArray : jsonArray) {
                    if (jsonElementInArray.isJsonPrimitive()) {
                        list.add(getValue(name, jsonElementInArray));
                    } else if (jsonElementInArray.isJsonObject()) {
                        list.add(makeObjectFromJsonMaker(jsonElementInArray.getAsJsonObject(), Class.forName(name)));
                    }
                }

                Field field = object.getClass().getDeclaredField(getFieldNameFromKey(key));
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, list);
                field.setAccessible(wasAccessible);
            }
        }
        return object;
    }

    private static Object getValue(String name, JsonElement jsonElement) {
        System.out.println("Debug name: " + name);
        JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        if (jsonPrimitive.isBoolean()) {
            return jsonPrimitive.getAsBoolean();
        } else if (jsonPrimitive.isNumber()) {
            try {
                Class<?> clazz = Class.forName(name);
                name = clazz.getSimpleName();
            } catch (ClassNotFoundException ignored) {
            }

            Number number = jsonElement.getAsNumber();
            switch (name.toLowerCase()) {
                case "int":
                case "integer":
                    return number.intValue();
                case "long":
                    return number.longValue();
                case "float":
                    return number.floatValue();
                case "double":
                    return number.doubleValue();
                case "biginteger":
                    return jsonElement.getAsBigInteger();
                case "bigdecimal":
                    return jsonElement.getAsBigDecimal();
            }

            return jsonPrimitive.getAsNumber();
        } else if (jsonPrimitive.isString()) {
            return jsonPrimitive.getAsString();
        }
        return null;
    }

    private static String getClassNameFromKey(String key) {
        int index = key.lastIndexOf("$");
        if (index + 2 <= key.length()) {
            return key.substring(index + 1);
        } else {
            return "";
        }
    }

    private static String getFieldNameFromKey(String key) {
        int index = key.lastIndexOf("$");
        if (index != 0) {
            return key.substring(0, index);
        } else {
            return "$";
        }
    }

    private static String getListType(String key) {
        int index = key.lastIndexOf("_");
        if (index + 2 <= key.length()) {
            return key.substring(index + 1);
        } else {
            return "";
        }
    }
}
