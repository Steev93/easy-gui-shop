package pers.zhangyang.easyguishop.executor;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.exception.NotExistIconException;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class SetIconVaultPriceExecutor extends ExecutorBase {

    public SetIconVaultPriceExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 3) {
            return;
        }
        double price;
        try {
            price = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            invalidArgument(args[2]);
            return;
        }
        if (price < 0) {
            invalidArgument(args[2]);
            return;
        }
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.setIconVaultPrice(args[1], price);


        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistIconException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notExistIconWhenSetIconVaultPrice"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.setIconVaultPrice"));
    }
}
