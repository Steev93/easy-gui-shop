package pers.zhangyang.easyguishop.executor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.exception.DuplicateIconException;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.*;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class CreateIconExecutor extends ExecutorBase {
    public CreateIconExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 2) {
            return;
        }
        Player player = (Player) sender;
        if (InventoryUtil.getItemInMainHand(player).getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
            return;
        }

        args[1]= ChatColor.translateAlternateColorCodes('&',args[1]);
        ItemStack itemStack = InventoryUtil.getItemInMainHand(player);
        IconMeta iconMeta = new IconMeta(UuidUtil.getUUID(), args[1], System.currentTimeMillis(), 0,
                ItemStackUtil.itemStackSerialize(itemStack), false);
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.createIcon(iconMeta);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (DuplicateIconException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.duplicateIconWhenCreate"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.createIcon"));
    }
}

