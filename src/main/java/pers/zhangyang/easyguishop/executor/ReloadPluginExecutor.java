package pers.zhangyang.easyguishop.executor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

public class ReloadPluginExecutor extends ExecutorBase {
    public ReloadPluginExecutor(@NotNull CommandSender sender, String cmdName, @NotNull String[] args) {
        super(sender, cmdName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(player, 54);
        inventory.setItem(0, new ItemStack(Material.AIR));
        System.out.println(inventory.getItem(0));
        player.openInventory(inventory);

        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.reloadPlugin"));
    }
}
