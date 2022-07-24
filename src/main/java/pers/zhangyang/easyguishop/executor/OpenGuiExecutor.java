package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.domain.AllShopPage;
import pers.zhangyang.easylibrary.base.ExecutorBase;

public class OpenGuiExecutor extends ExecutorBase {
    public OpenGuiExecutor(@NotNull CommandSender sender, String cmdName, @NotNull String[] args) {
        super(sender, cmdName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }
        Player player = (Player) sender;
        AllShopPage allShopPage = new AllShopPage(player, player);


        allShopPage.send();


    }
}
