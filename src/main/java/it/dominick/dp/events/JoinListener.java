package it.dominick.dp.events;

import it.dominick.dp.config.Messages;
import it.dominick.dp.database.DisguiseAsyncDB;
import it.dominick.dp.manager.DisguiseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final DisguiseManager disguiseManager;
    private DisguiseAsyncDB disguiseAsyncDB;

    public JoinListener(DisguiseManager disguiseManager, DisguiseAsyncDB disguiseDatabase) {
        this.disguiseManager = disguiseManager;
        this.disguiseAsyncDB = disguiseDatabase;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (!(disguiseAsyncDB.getPlayer(event.getPlayer().getName()) == null)) {
            String player = event.getPlayer().getName();
            String disguiseName = disguiseAsyncDB.getPlayer(player);
            disguiseManager.loadDisguiseInfo(disguiseName, ((texture, signature) -> {
                if (texture == null || signature == null) {
                    return;
                }

                disguiseAsyncDB.saveDisguiseAsync(player, disguiseName);
                disguiseManager.applyDisguise(event.getPlayer(), disguiseName, texture, signature);
                Messages.JOIN_ENABLE_DISGUISE.send(event.getPlayer(), "%fakeName%", disguiseName);
            }));
        }
    }
}
