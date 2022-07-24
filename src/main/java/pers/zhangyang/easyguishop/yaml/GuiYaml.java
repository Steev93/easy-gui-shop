package pers.zhangyang.easyguishop.yaml;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easylibrary.base.YamlBase;
import pers.zhangyang.easylibrary.exception.NotApplicableException;
import pers.zhangyang.easylibrary.exception.UnsupportedMinecraftVersionException;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.VersionUtil;

import java.util.ArrayList;
import java.util.List;

public class GuiYaml extends YamlBase {

    public static final GuiYaml INSTANCE = new GuiYaml();

    private GuiYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/gui.yml");
    }

    @NotNull
    public ItemStack getButton(@NotNull String path) {
        String materialName = getStringDefault(path + ".materialName");
        Material material = Material.matchMaterial(materialName);

        if (material == null) {
            materialName = backUpConfiguration.getString(path + ".materialName");
            assert materialName != null;
            material = Material.matchMaterial(materialName);
        }
        assert material != null;
        if (material.equals(Material.AIR)) {
            return new ItemStack(Material.AIR);
        }
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
        if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil
                .getMinecraftMiddleVersion() < 13) {
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
            } catch (UnsupportedMinecraftVersionException e) {
                //不会发生的
                e.printStackTrace();
                return null;
            }
        }
    }
}

