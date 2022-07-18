package pers.zhangyang.easyguishop.yaml;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyguishop.base.YamlBase;

public class SettingYaml extends YamlBase {

    public static final SettingYaml INSTANCE = new SettingYaml();

    private SettingYaml() {
        super("setting.yml");
    }

    @NotNull
    public String getDisplay() {
        String display = getStringDefault("setting.display");
        if (!display.equalsIgnoreCase("default")
                && !display.equalsIgnoreCase("english")) {
            display = backUpConfiguration.getString("setting.display");
        }
        return display;
    }

    @NotNull
    public Location getLocationMath(String path) {
        Double x = getDoubleDefault(path + ".x");
        Double y = getDoubleDefault(path + ".y");
        Double z = getDoubleDefault(path + ".z");
        String worldName = getStringDefault(path + ".worldName");
        World world = Bukkit.getWorld(worldName);
        return new Location(world == null ? Bukkit.getWorld("world") : world, x, y, z);
    }

    public double getRange(String path) {
        Double var = getDoubleDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
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
