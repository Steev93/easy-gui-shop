package pers.zhangyang.easyguishop.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.Nullable;

public class PermUtil {
    @Nullable
    public static Integer getNumberPerm(String startWith, Player player2) {
        Integer max = null;
        for (PermissionAttachmentInfo player : player2.getEffectivePermissions()) {
            if (player.getPermission().startsWith(startWith.toLowerCase())) {
                int end = player.getPermission().split("\\.").length;
                if (end != 0) {
                    try {
                        max = Integer.parseInt(player.getPermission().split("\\.")[end - 1]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    if (max < 0) {
                        break;
                    }
                }
            }
        }
        return max;
    }
}
