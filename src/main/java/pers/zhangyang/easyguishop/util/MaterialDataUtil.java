package pers.zhangyang.easyguishop.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MaterialDataUtil {

    @NotNull
    public static MaterialData deserializeMaterialData(@NotNull String data) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(data, mapType);

        Material material = Material.matchMaterial(map.get("type"));
        if (material == null) {
            throw new IllegalArgumentException();
        }
        return new MaterialData(material, Byte.parseByte(map.get("data")));
    }

    @NotNull
    public static String serializeMaterialData(@NotNull MaterialData materialData) {

        Map<String, String> m = new HashMap<>();
        return "{'type'=\"" + materialData.getItemType().name() + "\",'data'=" + materialData.getData() + "}";

    }
}
