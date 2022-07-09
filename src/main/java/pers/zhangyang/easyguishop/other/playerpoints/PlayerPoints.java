package pers.zhangyang.easyguishop.other.playerpoints;

import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;


public class PlayerPoints {
    public static PlayerPointsAPI hook() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            return ((org.black_ixx.playerpoints.PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
        }
        return null;
    }
}
