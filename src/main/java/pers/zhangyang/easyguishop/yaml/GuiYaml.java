package pers.zhangyang.easyguishop.yaml;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easyguishop.base.YamlBase;
import pers.zhangyang.easyguishop.exception.NotApplicableException;
import pers.zhangyang.easyguishop.util.ItemStackUtil;
import pers.zhangyang.easyguishop.util.MinecraftVersionUtil;

import java.util.ArrayList;
import java.util.List;

public class GuiYaml extends YamlBase {

    public static final GuiYaml INSTANCE = new GuiYaml();

    private GuiYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/gui.yml");
    }

    public ItemStack getButton(String path) {
        String materialName = getStringDefault(path + ".materialName");
        Material material = Material.matchMaterial(materialName);
        if (material == null || material.equals(Material.AIR)) {
            materialName = backUpConfiguration.getString(path + ".materialName");
            assert materialName != null;
            material = Material.matchMaterial(materialName);
        }
        assert material != null;
        String displayName = getString(path + ".displayName");
        List<String> lore = getStringList(path + ".lore");
        int amount = getIntegerDefault(path + ".amount");
        List<String> itemFlagName = getStringListDefault(path + ".itemFlag");
        List<ItemFlag> itemFlagList = new ArrayList<>();
        Integer customModelData = getInteger(path + ".amount");
        for (String s : itemFlagName) {
            try {
                itemFlagList.add(ItemFlag.valueOf(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (MinecraftVersionUtil.getBigVersion() == 1 && MinecraftVersionUtil
                .getMiddleVersion() < 13) {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            }
        } else {
            try {
                return ItemStackUtil.getItemStack(material, displayName, lore, itemFlagList, amount, customModelData);
            } catch (NotApplicableException e) {
                return new ItemStack(material);
            }
        }
    }
}

