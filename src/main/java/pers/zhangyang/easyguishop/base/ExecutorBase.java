package pers.zhangyang.easyguishop.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.ReplaceUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.util.Collections;
import java.util.List;

public abstract class ExecutorBase {

    protected boolean forcePlayer;

    protected CommandSender sender;

    protected String[] args;

    public ExecutorBase(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException();
        }
        this.sender = sender;
        this.forcePlayer = forcePlayer;
        this.args = args;
    }

    public void process() {
        if (!(sender instanceof Player) && forcePlayer) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notPlayer"));
            return;
        }
        String permission = "EasyGuiShop." + args[0];
        if (!sender.hasPermission(permission)) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notPermission");
            if (list != null) {
                ReplaceUtil.replace(list, Collections.singletonMap("{permission}", permission));
            }
            MessageUtil.sendMessageTo(sender, list);
            return;
        }
        run();
    }

    protected void invalidArgument(@NotNull String arg) {
        List<String> list = MessageYaml.INSTANCE.getStringList("invalidArgument");
        if (list != null) {
            ReplaceUtil.replace(list, Collections.singletonMap("{argument}", arg));
        }
        MessageUtil.sendMessageTo(sender, list);
    }

    protected abstract void run();

}

