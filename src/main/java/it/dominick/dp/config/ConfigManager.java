package it.dominick.dp.config;

import it.dominick.dp.DisguisePlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private Map<ConfigType, ConfigHandler> configurations;

    public ConfigManager() {
        configurations = new HashMap<>();
    }

    public void loadFiles(DisguisePlugin plugin) {

        registerFile(ConfigType.SETTINGS, new ConfigHandler(plugin, "config"));
        registerFile(ConfigType.MESSAGES, new ConfigHandler(plugin, "messages"));

        configurations.values().forEach(ConfigHandler::saveDefaultConfig);

        Messages.setConfiguration(getFile(ConfigType.MESSAGES).getConfig());
    }

    public ConfigHandler getFile(ConfigType type) {
        return configurations.get(type);
    }

    public void registerFile(ConfigType type, ConfigHandler config) {
        configurations.put(type, config);
    }

}
