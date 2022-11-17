package com.itayfeder.cloaked.reload;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CapeDataReloadListener extends SimpleJsonResourceReloadListener {
    public static final CapeDataReloadListener INSTANCE;
    public static final Gson GSON = new GsonBuilder().create();
    public final BiMap<ResourceLocation, CapeData> capeData;

    public CapeDataReloadListener() {
        super(GSON, "cape_data");
        this.capeData = HashBiMap.create();
    }

    static {
        INSTANCE = new CapeDataReloadListener();
    }

    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        capeData.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation name = entry.getKey();
            String[] split = name.getPath().split("/");
            if (split[split.length - 1].startsWith("_"))
                continue;
            JsonObject json = entry.getValue().getAsJsonObject();
            try {
                CapeData recipe = CapeData.deserialize(json);
                capeData.put(name, recipe);
            } catch (IllegalArgumentException | JsonParseException e) {
                System.out.println(String.format("I got an error!!!: ", e));
            }
        }

        System.out.println(String.format("%s Capes loaded successfully !", capeData.size()));
    }
}
