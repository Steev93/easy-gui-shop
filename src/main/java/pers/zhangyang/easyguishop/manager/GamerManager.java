package pers.zhangyang.easyguishop.manager;

import org.bukkit.entity.Player;
import pers.zhangyang.easyguishop.domain.Gamer;

import java.util.HashMap;

public class GamerManager {
    public static final GamerManager INSTANCE = new GamerManager();
    private final HashMap<Player, Gamer> playerGamerHashMap = new HashMap<>();

    public Gamer getGamer(Player player) {
        if (!playerGamerHashMap.containsKey(player)) {
            playerGamerHashMap.put(player, new Gamer(player));
        }
        return playerGamerHashMap.get(player);
    }

    public void remove(Player player) {
        this.playerGamerHashMap.remove(player);
    }
}
