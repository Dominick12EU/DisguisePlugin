package it.dominick.dp.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private final JavaPlugin plugin;
    private final String name;
    private final File file;
    private FileConfiguration configuration;

    public ConfigHandler(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        this.file = new File(plugin.getDataFolder(), this.name);
        this.configuration = new YamlConfiguration();
    }

    public void saveDefaultConfig() {
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }

        try {
            configuration.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    public FileConfiguration getConfig() {
        return configuration;
    }
}
