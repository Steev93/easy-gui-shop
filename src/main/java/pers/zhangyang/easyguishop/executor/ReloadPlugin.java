package pers.zhangyang.easyguishop.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.domain.*;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.CompleterYaml;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.io.IOException;
import java.sql.SQLException;

public class ReloadPlugin extends ExecutorBase {
    public ReloadPlugin(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {

        if (args.length != 1) {
            return;
        }
        try {
            SettingYaml.INSTANCE.init();
            CompleterYaml.INSTANCE.init();
            MessageYaml.INSTANCE.init();
            GuiYaml.INSTANCE.init();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }
        //初始化数据库
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();

        try {
            guiService.initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inventory = p.getOpenInventory().getTopInventory();
            InventoryHolder holder = inventory.getHolder();
            if ((holder instanceof AllGoodPage) || (holder instanceof AllGoodPageGoodOptionPage)
                    || (holder instanceof AllShopPage) || (holder instanceof AllShopPageShopOptionPage)
                    || (holder instanceof BuyIconPage) || (holder instanceof BuyIconPageIconOptionPage)
                    || (holder instanceof CollectedShopPage) || (holder instanceof CollectedShopPageShopOptionPage)
                    || (holder instanceof ManageCommentPage) || (holder instanceof ManageGoodPage)
                    || (holder instanceof ManageGoodPageGoodOptionPage) || (holder instanceof ManageIconPage)
                    || (holder instanceof ManageIconPageIconOptionPage) || (holder instanceof ManageItemStockPage)
                    || (holder instanceof ManageItemStockPageItemStockOptionPage) || (holder instanceof ManageShopPage)
                    || (holder instanceof ManageShopPageShopOptionPage) || (holder instanceof ManageTradeRecordPage)
                    || (holder instanceof ManageTradeRecordPageTradeRecordOptionPage) || (holder instanceof ShopCommentPage)) {
                p.closeInventory();
            }

        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.reloadPlugin"));
    }
}
