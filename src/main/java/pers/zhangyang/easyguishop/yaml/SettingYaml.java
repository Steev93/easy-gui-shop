package pers.zhangyang.easyguishop.yaml;

import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.YamlBase;

public class SettingYaml extends YamlBase {

    public static final SettingYaml INSTANCE = new SettingYaml();

    private SettingYaml() {
        super("setting.yml");
    }

    @NotNull
    public String getDisplay() {
        String display = getStringDefault("setting.display");
        if (pers.zhangyang.easylibrary.yaml.SettingYaml.class.getClassLoader().getResource("display/" + display) == null) {
            display = backUpConfiguration.getString("setting.display");
        }
        assert display != null;
        return display;
    }


    @NotNull
    public Double getTax(@NotNull String path) {
        double var = getDoubleDefault(path);
        if (var < 0 || var > 1) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

}
