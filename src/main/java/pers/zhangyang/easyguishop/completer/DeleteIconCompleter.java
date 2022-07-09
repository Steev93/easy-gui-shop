package pers.zhangyang.easyguishop.completer;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.CompleterBase;
import pers.zhangyang.easyguishop.yaml.CompleterYaml;

import java.util.ArrayList;
import java.util.List;

public class DeleteIconCompleter extends CompleterBase {
    public DeleteIconCompleter(@NotNull CommandSender sender, @NotNull String[] args) {
        super(sender, args);
    }

    @Override
    public @NotNull List<String> complete() {
        if (args.length == 2) {
            List<String> stringList = CompleterYaml.INSTANCE.getStringList("completer.easyGuiShopDeleteIcon");
            if (stringList != null) {
                return removeStartWith(args[1], stringList);
            }
        }
        return new ArrayList<>();
    }
}
