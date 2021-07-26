package lilmayu.mayusjsonutils.data;

import com.google.gson.JsonObject;

public interface ISavable<T> {

    JsonObject toJsonObject();

    T fromJsonObject(JsonObject jsonObject);
}
