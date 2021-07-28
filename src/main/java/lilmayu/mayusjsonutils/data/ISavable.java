package lilmayu.mayusjsonutils.data;

import com.google.gson.JsonObject;

public interface ISavable {

    JsonObject toJsonObject();

    void fromJsonObject(JsonObject jsonObject);
}
