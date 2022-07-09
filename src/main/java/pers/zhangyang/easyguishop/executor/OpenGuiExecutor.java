package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.domain.AllShopPage;

import java.sql.SQLException;

public class OpenGuiExecutor extends ExecutorBase {
    public OpenGuiExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }
        Player player = (Player) sender;
        AllShopPage allShopPage = new AllShopPage(player);

        try {
            allShopPage.send();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
