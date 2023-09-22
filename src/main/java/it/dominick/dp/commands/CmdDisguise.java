package it.dominick.dp.commands;

import it.dominick.dp.DisguisePlugin;
import it.dominick.dp.config.Messages;
import it.dominick.dp.database.DisguiseAsyncDB;
import it.dominick.dp.manager.Disguise;
import it.dominick.dp.manager.DisguiseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class CmdDisguise implements CommandExecutor {
    private DisguisePlugin plugin;
    private DisguiseAsyncDB disguiseAsyncDB;
    private DisguiseManager disguiseManager;

    public CmdDisguise(DisguisePlugin plugin, DisguiseAsyncDB disguiseAsyncDB, DisguiseManager disguiseManager) {
        this.plugin = plugin;
        this.disguiseAsyncDB = disguiseAsyncDB;
        this.disguiseManager = disguiseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            List<String> disguiseNames = plugin.getConfig().getStringList("disguise-names");

            if (disguiseNames.isEmpty()) {
                Messages.ERROR_EMPTY_LIST.send(player);
                return true;
            }

            Random random = new Random();
            String randomName = disguiseNames.get(random.nextInt(disguiseNames.size()));

            disguiseManager.loadDisguiseInfo(randomName, ((texture, signature) -> {
                if (texture == null || signature == null) {
                    Messages.ERROR_DISGUISE.send(player, "%skinName%", randomName);
                    return;
                }

                disguiseAsyncDB.saveDisguiseAsync(player.getName(), randomName);
                disguiseManager.applyDisguise(player, randomName, texture, signature);
                Messages.ENABLE_DISGUISE.send(player, "%fakeName%", randomName);
            }));
            return true;
        }

        String arg = args[0];

        if (arg.equalsIgnoreCase("clear")) {
            disguiseManager.deleteDisguise(player);
            disguiseAsyncDB.removeDisguiseAsync(player.getName());
            Messages.DISABLE_DISGUISE.send(player);
            return true;
        }

        if (arg.equalsIgnoreCase("list")) {
            disguiseManager.getDisguisedPlayers().forEach(disguisedPlayer -> {
                String fakeName = disguisedPlayer.getName();
                String realName = disguiseManager.getDisguise(disguisedPlayer)
                        .map(Disguise::getName)
                        .orElse(fakeName);
                Messages.LIST_FORMAT_DISGUISE.send(player, "%realName%", realName, "%fakeName%", fakeName);
            });
            return true;
        }

        return true;
    }
}
