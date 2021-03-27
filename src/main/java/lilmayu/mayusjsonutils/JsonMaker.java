package lilmayu.mayusjsonutils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lilmayu.mayusjsonutils.annotations.SerializableJson;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class JsonMaker {

    public static JsonObject makeJsonObject(Object object) throws IllegalAccessException {
        JsonObject jsonObject = new JsonObject();

        for (Field fieldInClassObject : object.getClass().getDeclaredFields()) {
            if (fieldInClassObject.isAnnotationPresent(SerializableJson.class)) {
                fieldInClassObject.setAccessible(true);
                addToJsonObject(jsonObject, object, fieldInClassObject);
            }
        }
        return jsonObject;
    }

    private static void addToJsonObject(JsonObject jsonObject, Object classObject, Field fieldInClassObject) throws IllegalAccessException {
        Object objectFromClassByField = fieldInClassObject.get(classObject);
        String fieldNameFromClassByField = fieldInClassObject.getName();

        JsonElement jsonPrimitive = getJsonElement(objectFromClassByField);

        if (fieldInClassObject.getType().getSimpleName().equalsIgnoreCase("list")) {
            ParameterizedType listType = (ParameterizedType) fieldInClassObject.getGenericType();
            jsonObject.add(fieldNameFromClassByField + "$" + fieldInClassObject.getType().getName() + "_" + listType.getActualTypeArguments()[0].getTypeName(), jsonPrimitive);
        } else {
            jsonObject.add(fieldNameFromClassByField + "$" + fieldInClassObject.getType().getName(), jsonPrimitive);
        }
    }

    private static JsonElement getJsonElement(Object objectFromClassByField) throws IllegalAccessException {
        if (objectFromClassByField instanceof Number) {
            return new JsonPrimitive((Number) objectFromClassByField);
        } else if (objectFromClassByField instanceof Boolean) {
            return new JsonPrimitive((Boolean) objectFromClassByField);
        } else if (objectFromClassByField instanceof String) {
            return new JsonPrimitive((String) objectFromClassByField);
        } else if (objectFromClassByField instanceof Character) {
            return new JsonPrimitive((Character) objectFromClassByField);
        } else if (objectFromClassByField instanceof List) {
            JsonArray jsonArray = new JsonArray();

            for (Object object : (List<?>) objectFromClassByField) {
                if (object instanceof Number) {
                    jsonArray.add((Number) object);
                } else if (object instanceof Boolean) {
                    jsonArray.add((Boolean) object);
                } else if (object instanceof String) {
                    jsonArray.add((String) object);
                } else if (object instanceof Character) {
                    jsonArray.add((Character) object);
                } else {
                    JsonObject jsonObject = makeJsonObject(object);
                    jsonArray.add(jsonObject);
                }
            }
            return jsonArray;
        } else {
            return makeJsonObject(objectFromClassByField);
        }
    }
}

