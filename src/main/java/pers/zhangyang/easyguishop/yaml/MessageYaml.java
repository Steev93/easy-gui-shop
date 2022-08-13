package pers.zhangyang.easyguishop.yaml;

import pers.zhangyang.easylibrary.base.YamlBase;

public class MessageYaml extends YamlBase {

    public static final MessageYaml INSTANCE = new MessageYaml();

    private MessageYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/message.yml");
    }


}
