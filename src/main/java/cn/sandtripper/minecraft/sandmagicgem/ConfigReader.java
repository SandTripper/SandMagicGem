package cn.sandtripper.minecraft.sandmagicgem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigReader {
    private final JavaPlugin plugin;
    private final String configName;
    private File configFile;
    private FileConfiguration config;

    public ConfigReader(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        this.configName = filename;
        this.configFile = new File(plugin.getDataFolder(), filename);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(configName, false);
        }
    }

    public FileConfiguration getConfig() {
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
        if (config == null) {
            this.reloadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configName);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource(configName);
        if (defaultStream != null) {
            YamlConfiguration defaultFile = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultFile);
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Throwable t) {
            Bukkit.getLogger().warning("配置保存失败!请重试!");
        }
    }
}