package pers.zhangyang.easyguishop.util;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;


public class LocationUtil {

    public static boolean isDifferentWorld(@NotNull Location location1, @NotNull Location location2) {
        if (location1.getWorld() == null || location2.getWorld() == null) {
            return true;
        }
        return !location1.getWorld().getUID().equals(location2.getWorld().getUID());
    }

    //按方块
    public static boolean blockIsIn(@NotNull Location corner1, @NotNull Location corner2, @NotNull Location target) {
        if (isDifferentWorld(corner1, corner2) || isDifferentWorld(corner1, target) || isDifferentWorld(corner2, target)) {
            return false;
        }
        int corner1X = corner1.getBlockX();
        int corner1Y = corner1.getBlockY();
        int corner1Z = corner1.getBlockZ();
        int corner2X = corner2.getBlockX();
        int corner2Y = corner2.getBlockY();
        int corner2Z = corner2.getBlockZ();
        int targetX = target.getBlockX();
        int targetY = target.getBlockY();
        int targetZ = target.getBlockZ();
        return targetX >= Math.min(corner1X, corner2X) && targetX <= Math.max(corner1X, corner2X)
                && targetY >= Math.min(corner1Y, corner2Y) && targetY <= Math.max(corner1Y, corner2Y)
                && targetZ >= Math.min(corner1Z, corner2Z) && targetZ <= Math.max(corner1Z, corner2Z);
    }

    public static String serializeLocation(@NotNull Location meta) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("location", meta);
        return yml.saveToString();
    }

    public static Location deserializeLocation(@NotNull String data) {
        YamlConfiguration yml = new YamlConfiguration();
        try {
            yml.loadFromString(data);
        } catch (InvalidConfigurationException e) {
            throw new IllegalArgumentException();
        }
        Object obj = yml.get("location");

        if (obj == null) {
            throw new IllegalArgumentException();
        }
        return (Location) obj;
    }

}
