package pers.zhangyang.easyguishop.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.runnable.NotifyVersionRunnable;

public class PlayerJoin implements Listener {
    @EventHandler
    public void on(PlayerJoinEvent event){
        Player player= event.getPlayer();
        //提示版本
        if (player.hasPermission("EasyGuiShop.receiveVersionInformation")){
            new NotifyVersionRunnable(player).runTaskAsynchronously(EasyGuiShop.instance);
        }
    }
}
