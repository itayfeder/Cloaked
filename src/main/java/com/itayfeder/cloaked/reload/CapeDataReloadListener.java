package com.itayfeder.cloaked.reload;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import com.itayfeder.cloaked.Cloaked;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CapeDataReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final CapeDataReloadListener INSTANCE;
    public static final Gson GSON = new GsonBuilder().create();
    public final BiMap<Identifier, CapeData> capeData;

    public CapeDataReloadListener() {
        super(GSON, "cape_data");
        this.capeData = HashBiMap.create();
    }

    static {
        INSTANCE = new CapeDataReloadListener();
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        capeData.clear();
        for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
            Identifier name = entry.getKey();
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


    @Override
    public Identifier getFabricId() {
        return new Identifier(Cloaked.MODID, "cape_data");
    }
}
