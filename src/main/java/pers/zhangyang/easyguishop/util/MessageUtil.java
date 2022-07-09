package pers.zhangyang.easyguishop.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class MessageUtil {

    public static void sendTitleTo(@NotNull Player player, @Nullable String title, @Nullable String subtitle) {
        if (title != null) {
            title = ChatColor.translateAlternateColorCodes('&', title);
        }
        if (subtitle != null) {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        }
        player.sendTitle(title, subtitle, 10, 10, 20);
    }

    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable List<String> strings) {
        if (strings == null) {
            return;
        }
        for (String s : strings) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable List<String> strings) {
        if (strings == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            for (String s : strings) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
    }

    public static void sendMessageTo(@NotNull Collection<? extends CommandSender> senderList, @Nullable String s) {
        if (s == null) {
            return;
        }
        for (CommandSender sender : senderList) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }

    public static void sendMessageTo(@NotNull CommandSender sender, @Nullable String s) {
        if (s == null) {
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

}
