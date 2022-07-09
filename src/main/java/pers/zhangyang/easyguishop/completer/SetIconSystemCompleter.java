package pers.zhangyang.easyguishop.completer;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.CompleterBase;
import pers.zhangyang.easyguishop.yaml.CompleterYaml;

import java.util.ArrayList;
import java.util.List;

public class SetIconSystemCompleter extends CompleterBase {
    public SetIconSystemCompleter(@NotNull CommandSender sender, @NotNull String[] args) {
        super(sender, args);
    }

    @Override
    public @NotNull List<String> complete() {
        if (args.length == 2) {
            List<String> stringList = CompleterYaml.INSTANCE.getStringList("completer.easyGuiShopSetIconSystem");
            if (stringList != null) {
                return removeStartWith(args[1], stringList);
            }
        }
        if (args.length == 3) {
            List<String> stringList = CompleterYaml.INSTANCE.getStringList("completer.easyGuiShopSetIconSystem$");
            if (stringList != null) {
                return removeStartWith(args[2], stringList);
            }
        }
        return new ArrayList<>();
    }
}