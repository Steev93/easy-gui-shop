package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.exception.NotMorePopularityException;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class SubtractShopPopularityExecutor extends ExecutorBase {
    public SubtractShopPopularityExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 3) {
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            invalidArgument(args[2]);
            return;
        }
        if (amount < 0) {
            invalidArgument(args[2]);
            return;
        }
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.subtractShopPopularity(args[1], amount);


        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notExistShopWhenSubtractPopularity"));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotMorePopularityException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notMorePopularity"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.subtractShopPopularity"));
    }
}
