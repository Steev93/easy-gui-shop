package pers.zhangyang.easyguishop.yaml;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        if (!display.equalsIgnoreCase("default")
                && !display.equalsIgnoreCase("en_us")
                && !display.equalsIgnoreCase("zh_cn")) {
            display = backUpConfiguration.getString("setting.display");
        }
        assert display != null;
        return display;
    }

    @NotNull
    public Location getLocation(@NotNull String path) {
        Double x = getDoubleDefault(path + ".x");
        Double y = getDoubleDefault(path + ".y");
        Double z = getDoubleDefault(path + ".z");
        Double yaw = getDoubleDefault(path + ".yaw");
        Double pitch = getDoubleDefault(path + ".pitch");
        String worldName = getStringDefault(path + ".worldName");
        World world = Bukkit.getWorld(worldName);
        return new Location(world == null ? Bukkit.getWorld("world") : world, x, y, z, yaw.floatValue(), pitch.floatValue());
    }

    public double getRange(@NotNull String path) {
        double var = getDoubleDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

    public int getHotValueCoefficient(@NotNull String path) {
        int var = getIntegerDefault(path);
        if (var < 0) {
            var = backUpConfiguration.getInt(path);
        }
        return var;
    }

    public double getTax(@NotNull String path) {
        double var = getDoubleDefault(path);
        if (var < 0 || var > 1) {
            var = backUpConfiguration.getDouble(path);
        }
        return var;
    }

}
