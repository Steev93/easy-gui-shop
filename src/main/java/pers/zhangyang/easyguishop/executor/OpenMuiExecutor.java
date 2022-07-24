package pers.zhangyang.easyguishop.executor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easylibrary.base.ExecutorBase;

public class OpenMuiExecutor extends ExecutorBase {
    public OpenMuiExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        Player player = (Player) sender;
        AllShopPage allShopPage = new AllShopPage(player, offlinePlayer);
        allShopPage.send();

    }
}
