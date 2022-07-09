package pers.zhangyang.easyguishop.yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.base.YamlBase;

public class MessageYaml extends YamlBase {

    public static final MessageYaml INSTANCE = new MessageYaml();

    private MessageYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/message.yml");
    }

    @Nullable
    public String getInput(@NotNull String path) {
        String s = getStringDefault(path);
        if (s.isEmpty()) {
            s = backUpConfiguration.getString(path);
        }
        return s;
    }
}
