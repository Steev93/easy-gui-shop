package pers.zhangyang.easyguishop.domain;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Gamer {
    private final Player player;

    private Long lastTeleportShopLocationTime;

    public Gamer(Player player) {
        this.player = player;
    }

    @Nullable
    public Long getLastTeleportShopLocationTime() {
        return lastTeleportShopLocationTime;
    }

    public void setLastTeleportShopLocationTime(Long lastTeleportShopLocationTime) {
        this.lastTeleportShopLocationTime = lastTeleportShopLocationTime;
    }

    public Player getPlayer() {
        return player;
    }
}
