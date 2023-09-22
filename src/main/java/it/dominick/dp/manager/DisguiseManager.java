package it.dominick.dp.manager;

import it.dominick.dp.utils.HTTPCallbacks;
import it.dominick.dp.utils.HTTPUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class DisguiseManager {
    private final HTTPUtility httpUtility;
    private final Map<UUID, Disguise> playerIdToDisguise = new HashMap<>();

    public DisguiseManager(HTTPUtility httpUtility) {
        this.httpUtility = httpUtility;
    }

    public void loadDisguiseInfo(String playerName, HTTPCallbacks.GetTextureResponse response) {
        httpUtility.getTextureAndSignature(playerName, response);
    }

    public void applyDisguise(Player player, String name, String texture, String signature) {
        if (hasDisguise(player)) {
            deleteDisguise(player);
        }

        Disguise disguise = new Disguise(name, texture, signature);
        playerIdToDisguise.put(player.getUniqueId(), disguise);
        disguise.apply(player);
    }

    public void deleteDisguise(Player player) {
        if (!hasDisguise(player)) return;
        Disguise existing = getDisguise(player).get();
        existing.remove(player);
        playerIdToDisguise.remove(player.getUniqueId());
    }

    public Optional<Disguise> getDisguise(Player player) {
        return Optional.ofNullable(
                playerIdToDisguise.get(player.getUniqueId())
        );
    }

    public List<Player> getDisguisedPlayers() {
        List<Player> disguisedPlayers = new ArrayList<>();
        for (Map.Entry<UUID, Disguise> entry : playerIdToDisguise.entrySet()) {
            UUID playerId = entry.getKey();
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                disguisedPlayers.add(player);
            }
        }
        return disguisedPlayers;
    }

    public boolean hasDisguise(Player player) {
        return playerIdToDisguise.containsKey(player.getUniqueId());
    }
}
