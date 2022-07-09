package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

public class HelpExecutor extends ExecutorBase {
    public HelpExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.help"));
    }
}
