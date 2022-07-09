package pers.zhangyang.easyguishop.yaml;

import pers.zhangyang.easyguishop.base.YamlBase;

public class SettingYaml extends YamlBase {

    public static final SettingYaml INSTANCE = new SettingYaml();

    private SettingYaml() {
        super("setting.yml");
    }

    public String getDisplay() {
        String display = getStringDefault("setting.display");
        if (!display.equalsIgnoreCase("default")) {
            display = backUpConfiguration.getString("setting.display");
        }
        return display;
    }

    public int getHotValueCoefficient(String path) {
        Integer var = getIntegerDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getInt(path);
        }
        return var;
    }

    public double getTax(String path) {
        Double var = getDoubleDefault(path);
        if (var < 0 || var > 1) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

}
