package pers.zhangyang.easyguishop.executor;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.ExecutorBase;
import pers.zhangyang.easyguishop.exception.NotExistGoodException;
import pers.zhangyang.easyguishop.exception.NotExistShopException;
import pers.zhangyang.easyguishop.service.CommandService;
import pers.zhangyang.easyguishop.service.impl.CommandServiceImpl;
import pers.zhangyang.easyguishop.util.MessageUtil;
import pers.zhangyang.easyguishop.util.TransactionInvocationHandler;
import pers.zhangyang.easyguishop.yaml.MessageYaml;

import java.sql.SQLException;

public class SetGoodSystemExecutor extends ExecutorBase {

    public SetGoodSystemExecutor(@NotNull CommandSender sender, boolean forcePlayer, @NotNull String[] args) {
        super(sender, forcePlayer, args);
    }

    @Override
    protected void run() {
        if (args.length != 4) {
            return;
        }
        if (!args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {

            invalidArgument(args[3]);
            return;
        }
        args[1]= ChatColor.translateAlternateColorCodes('&',args[1]);
        args[2]= ChatColor.translateAlternateColorCodes('&',args[2]);
        boolean system = Boolean.parseBoolean(args[3]);
        CommandService guiService = (CommandService) new TransactionInvocationHandler(CommandServiceImpl.INSTANCE).getProxy();
        try {
            guiService.setGoodSystem(args[1], args[2], system);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (NotExistShopException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notExistShopWhenSetGoodSystem"));
            return;
        } catch (NotExistGoodException e) {
            MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.notExistGoodWhenSetGoodSystem"));
            return;
        }
        MessageUtil.sendMessageTo(sender, MessageYaml.INSTANCE.getStringList("message.chat.setGoodSystem"));
    }
}
