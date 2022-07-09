package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class SetShopSystemExecutor extends ExecutorBase {

    public SetShopSystemExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 3) {
            return;
        }
        if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {

            invalidArgument(args[2]);
            return;
        }
        boolean system = Boolean.parseBoolean(args[2]);
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.setShopSystem(args[1], system);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notExistShopWhenSetShopSystem"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.setShopSystem"));
    }
}