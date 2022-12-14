package com.itayfeder.cloaked.reload;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;

public class CapeData {
    public String translationName;
    public boolean treasure;

    public CapeData(String translationName, boolean treasure) {
        this.translationName = translationName;
        this.treasure = treasure;
    }

    @Override
    public String toString() {
        return String.format("CapeData[translationName: %s, treasure: %s]", this.translationName, this.treasure);
    }

    public static CapeData deserialize(JsonObject json) {
        if (!json.isJsonObject())
            throw new JsonParseException("CapeData must be a JSON Object");

        JsonObject jsonObject = json.getAsJsonObject();
        String translationName = GsonHelper.getAsString(jsonObject, "translationName", null);
        if (translationName == null)
            throw new JsonParseException("translationName is not valid");

        boolean treasure = GsonHelper.getAsBoolean(jsonObject, "treasure", true);

        return new CapeData(translationName, treasure);

    }
}
