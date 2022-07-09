package pers.zhangyang.easyguishop.yaml;

import pers.zhangyang.easyguishop.base.YamlBase;

public class CompleterYaml extends YamlBase {

    public static final CompleterYaml INSTANCE = new CompleterYaml();

    private CompleterYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/completer.yml");
    }

}
