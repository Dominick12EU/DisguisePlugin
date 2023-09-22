package it.dominick.dp;

import it.dominick.dp.commands.CmdDisguise;
import it.dominick.dp.config.ConfigManager;
import it.dominick.dp.config.ConfigType;
import it.dominick.dp.database.DisguiseAsyncDB;
import it.dominick.dp.database.DisguiseDatabase;
import it.dominick.dp.events.JoinListener;
import it.dominick.dp.manager.DisguiseManager;
import it.dominick.dp.utils.HTTPUtility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class DisguisePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private DisguiseDatabase disguiseDatabase;
    private DisguiseAsyncDB disguiseAsyncDB;
    private DisguiseManager disguiseManager;


    @Override
    public void onEnable() {
        configManager = new ConfigManager();
        configManager.loadFiles(this);
        try {
            String host = getConfig().getString("database.host");
            int port = getConfig().getInt("database.port");
            String database = getConfig().getString("database.name");
            String username = getConfig().getString("database.username");
            String password = getConfig().getString("database.password");

            disguiseDatabase = new DisguiseDatabase(host, port, database, username, password);
            disguiseDatabase.connect();
            disguiseDatabase.createDisguiseTable();

            disguiseAsyncDB = new DisguiseAsyncDB(disguiseDatabase);

            HTTPUtility httpUtility = new HTTPUtility(this);
            disguiseManager = new DisguiseManager(httpUtility);

            getCommand("disguise").setExecutor(new CmdDisguise(this, disguiseAsyncDB, disguiseManager));
            getServer().getPluginManager().registerEvents(new JoinListener(disguiseManager, disguiseAsyncDB), this);
        } catch (Exception e) {
            getLogger().severe("Error activating the plugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        disguiseDatabase.close();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FileConfiguration getConfig() {
        return getConfigManager().getFile(ConfigType.SETTINGS).getConfig();
    }
}
