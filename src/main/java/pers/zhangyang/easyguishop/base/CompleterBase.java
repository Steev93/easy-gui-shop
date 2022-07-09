package pers.zhangyang.easyguishop.base;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CompleterBase {


    protected CommandSender sender;

    protected String[] args;

    public CompleterBase(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException();
        }
        this.sender = sender;
        this.args = args;
    }

    @NotNull
    protected List<String> removeStartWith(@Nullable String latest, @NotNull List<String> list) {
        if (latest == null) {
            return list;
        }
        String ll = latest.toLowerCase();
        list.removeIf(k -> !k.toLowerCase().startsWith(ll));
        return list;
    }

    @NotNull
    public List<String> process() {
        if (!sender.hasPermission(args[0])) {
            return new ArrayList<>();
        }
        return complete();
    }

    @NotNull
    public abstract List<String> complete();
}
