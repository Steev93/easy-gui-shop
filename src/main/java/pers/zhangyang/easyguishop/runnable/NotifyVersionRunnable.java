package pers.zhangyang.easyguishop.runnable;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.util.ResourceUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class NotifyVersionRunnable extends BukkitRunnable {
    private final CommandSender sender;

    public NotifyVersionRunnable(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        String latestVersion;
        try {
            latestVersion = ResourceUtil.readFirstLine(new URL("https://zhangyang0204.github.io/easy-gui-shop/index.html"));
        } catch (Throwable e) {
            latestVersion = null;
        }
        List<String> list;
        if (latestVersion != null) {
            list = MessageYaml.INSTANCE.getStringList("notifyVersion");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("{current}", EasyGuiShop.instance.getDescription().getVersion());
            hashMap.put("{latest}", latestVersion);
            if (list != null) {
                ReplaceUtil.replace(list, hashMap);
            }
        } else {
            list = MessageYaml.INSTANCE.getStringList("failureGetLatestVersion");
        }
        MessageUtil.sendMessageTo(sender, list);

    }


}
