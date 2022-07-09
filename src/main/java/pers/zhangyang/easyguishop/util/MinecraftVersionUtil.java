package pers.zhangyang.easyguishop.util;

import org.bukkit.Bukkit;

public class MinecraftVersionUtil {

    public static int getBigVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[0]);
    }

    public static int getMiddleVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("\\.")[1].split("-")[0]);
    }

}
