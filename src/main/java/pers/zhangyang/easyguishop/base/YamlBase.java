package pers.zhangyang.easyguishop.base;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyguishop.EasyGuiShop;
import pers.zhangyang.easyguishop.yaml.SettingYaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public abstract class YamlBase {

    protected YamlConfiguration yamlConfiguration;

    protected String filePath;

    protected YamlConfiguration backUpConfiguration;

    /**
     * @param filePath 在resource下的文件路径
     */
    protected YamlBase(@NotNull String filePath) {
        this.filePath = filePath;
        this.yamlConfiguration = new YamlConfiguration();
        this.backUpConfiguration = new YamlConfiguration();
    }

    /**
     * 会把对应的文件保存到PluginEasyGuiShop下
     *
     * @throws IOException                   IO异常
     * @throws InvalidConfigurationException Yml文件格式不对
     */
    public void init() throws IOException, InvalidConfigurationException {
        File file = new File(EasyGuiShop.instance.getDataFolder(), filePath);
        // 如果文件不存在就创建
        if (!file.exists()) {
            File dir = file.getParentFile();
            //先创建目录文件夹
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new IOException();
                }
            }
            //输出数据
            InputStream in = SettingYaml.class.getClassLoader().getResourceAsStream(filePath);
            if (in == null) {
                throw new IOException();
            }
            OutputStream out = Files.newOutputStream(file.toPath());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        //加载Yaml
        this.yamlConfiguration.load(file);
        InputStream in = YamlBase.class.getClassLoader().getResourceAsStream(filePath);
        if (in == null) {
            throw new IOException();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        this.backUpConfiguration.load(inputStreamReader);
    }

    /**
     * 修正配置文件内容
     *
     * @throws IOException io异常
     */
    public void correct() throws IOException {
        //删除多余的
        for (String path : yamlConfiguration.getKeys(true)) {
            if (!backUpConfiguration.getKeys(true).contains(path)) {
                Object ob = yamlConfiguration.get(path);
                yamlConfiguration.set(path, null);
                try {
                    yamlConfiguration.save(EasyGuiShop.instance.getDataFolder().getAbsoluteFile() + "/" + filePath);
                } catch (IOException e) {
                    yamlConfiguration.set(path, ob);
                    throw e;
                }
            }
        }
        //补充缺失的
        for (String pathBase : backUpConfiguration.getKeys(true)) {
            if (!yamlConfiguration.getKeys(true).contains(pathBase)) {
                Object ob = yamlConfiguration.get(pathBase);
                yamlConfiguration.set(pathBase, backUpConfiguration.get(pathBase));
                try {
                    yamlConfiguration.save(EasyGuiShop.instance.getDataFolder().getAbsoluteFile() + "/" + filePath);
                } catch (IOException e) {
                    yamlConfiguration.set(pathBase, ob);
                    throw e;
                }
            }
        }
    }


    @Nullable
    public Boolean getBoolean(@NotNull String path) {
        if (!yamlConfiguration.isBoolean(path)) {
            return null;
        }
        return yamlConfiguration.getBoolean(path);
    }

    @NotNull
    public Boolean getBooleanDefault(@NotNull String path) {
        if (!yamlConfiguration.isBoolean(path)) {
            return backUpConfiguration.getBoolean(path);
        }
        return yamlConfiguration.getBoolean(path);
    }

    @NotNull
    public String getStringDefault(@NotNull String path) {
        if (!yamlConfiguration.isString(path)) {
            return Objects.requireNonNull(backUpConfiguration.getString(path));
        }
        return Objects.requireNonNull(yamlConfiguration.getString(path));
    }

    @Nullable
    public String getString(@NotNull String path) {
        if (!yamlConfiguration.isString(path)) {
            return null;
        }
        return yamlConfiguration.getString(path);
    }

    @Nullable
    public Integer getInteger(@NotNull String path) {
        if (!yamlConfiguration.isInt(path)) {
            return null;
        }
        return yamlConfiguration.getInt(path);
    }

    @NotNull
    public Integer getIntegerDefault(@NotNull String path) {
        if (!yamlConfiguration.isInt(path)) {
            return backUpConfiguration.getInt(path);
        }
        return yamlConfiguration.getInt(path);
    }

    @Nullable
    public Long getLong(@NotNull String path) {
        if (!yamlConfiguration.isLong(path)) {
            return null;
        }
        return yamlConfiguration.getLong(path);
    }

    @NotNull
    public Long getLongDefault(@NotNull String path) {
        if (!yamlConfiguration.isLong(path)) {
            return backUpConfiguration.getLong(path);
        }
        return yamlConfiguration.getLong(path);
    }

    @Nullable
    public Double getDouble(@NotNull String path) {
        if (!yamlConfiguration.isDouble(path)) {
            return null;
        }
        return yamlConfiguration.getDouble(path);
    }

    @NotNull
    public Double getDoubleDefault(@NotNull String path) {
        if (!yamlConfiguration.isDouble(path)) {
            return backUpConfiguration.getDouble(path);
        }
        return yamlConfiguration.getDouble(path);
    }

    @Nullable
    public List<String> getStringList(@NotNull String path) {
        if (!yamlConfiguration.isList(path)) {
            return null;
        }
        return yamlConfiguration.getStringList(path);
    }

    @NotNull
    public List<String> getStringListDefault(@NotNull String path) {
        if (!yamlConfiguration.isList(path)) {
            return backUpConfiguration.getStringList(path);
        }
        return yamlConfiguration.getStringList(path);
    }

}
