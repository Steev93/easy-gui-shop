package pers.zhangyang.easyguishop.executor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.exception.DuplicateIconException;
import pers.zhangyang.easyguishop.meta.IconMeta;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.*;

import java.util.List;

public class CreateIconExecutor extends ExecutorBase {
    public CreateIconExecutor(@NotNull CommandSender sender, String cmdName, @NotNull String[] args) {
        super(sender, cmdName, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }

        if (!(sender instanceof Player)) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notPlayer");
            MessageUtil.sendMessageTo(this.sender, list);
            return;
        }
        Player player = (Player) sender;
        if (PlayerUtil.getItemInMainHand(player).getType().equals(Material.AIR)) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notItemInMainHand"));
            return;
        }

        ItemStack itemStack = PlayerUtil.getItemInMainHand(player);
        IconMeta iconMeta = new IconMeta(UuidUtil.getUUID(), args[0], System.currentTimeMillis(), 0,
                ItemStackUtil.itemStackSerialize(itemStack), false);
        CommandService guiService = (CommandService) new TransactionInvocationHandler(new CommandServiceImpl()).getProxy();
        try {
            guiService.createIcon(iconMeta);
        } catch (DuplicateIconException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.duplicateIconWhenCreate"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.createIcon"));
    }
}

