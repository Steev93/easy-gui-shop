package pers.zhangyang.easyguishop.util;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class NotifyVersionUtil {


    public static void notifyVersion(CommandSender sender) {

        new BukkitRunnable() {
            @Override
            public void run() {
                String latestVersion;
                try {
                    latestVersion = ResourceUtil.readFirstLine(new URL("https://zhangyang0204.github.io/easy-gui-shop/index.html"));
                } catch (Throwable e) {
                    latestVersion = null;
                }
                String finalLatestVersion = latestVersion;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        List<String> list;
                        if (finalLatestVersion != null) {
                            list = MessageYaml.INSTANCE.getStringList("message.chat.notifyVersion");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("{current_version}", EasyGuiShop.instance.getDescription().getVersion());
                            hashMap.put("{latest_version}", finalLatestVersion);
                            if (list != null) {
                                ReplaceUtil.replace(list, hashMap);
                            }
                        } else {
                            list = MessageYaml.INSTANCE.getStringList("message.chat.failureGetLatestVersion");
                        }
                        MessageUtil.sendMessageTo(sender, list);
                    }
                }.runTask(EasyGuiShop.instance);

            }
        }.runTaskAsynchronously(EasyGuiShop.instance);

    }

}
