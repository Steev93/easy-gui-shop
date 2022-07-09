package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class CorrectDatabaseExecutor extends ExecutorBase {
    public CorrectDatabaseExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 1) {
            return;
        }

        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.correctDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.correctDatabase"));
    }
}