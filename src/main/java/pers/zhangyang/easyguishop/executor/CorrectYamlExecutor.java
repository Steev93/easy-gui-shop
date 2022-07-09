package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.yaml.CompleterYaml;
import pers.zhangyang.easyguishop.yaml.GuiYaml;
import pers.zhangyang.easyguishop.yaml.MessageYaml;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.io.IOException;

public class CorrectYamlExecutor extends ExecutorBase {

    public CorrectYamlExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }
        try {
            SettingYaml.INSTANCE.correct();
            GuiYaml.INSTANCE.correct();
            MessageYaml.INSTANCE.correct();
            CompleterYaml.INSTANCE.correct();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.correctYaml"));
    }
}
