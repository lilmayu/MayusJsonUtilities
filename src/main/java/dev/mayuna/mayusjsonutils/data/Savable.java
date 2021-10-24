package dev.mayuna.mayusjsonutils.data;

import com.google.gson.JsonObject;

public interface Savable {

    JsonObject toJsonObject();

    void fromJsonObject(JsonObject jsonObject);
}
