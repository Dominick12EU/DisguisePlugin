package it.dominick.dp.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisguiseAsyncDB {

    private DisguiseDatabase database;
    private ExecutorService executorService;

    public DisguiseAsyncDB(DisguiseDatabase database) {
        this.database = database;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void saveDisguiseAsync(String player, String fakename) {
        executorService.execute(() -> {
            if (getPlayer(player) != null) {
                return;
            }

            database.saveDisguise(player, fakename);
        });
    }

    public void removeDisguiseAsync(String player) {
        executorService.execute(() -> {
            database.removeDisguise(player);
        });
    }

    public String getPlayer(String player) {
        return database.getPlayer(player);
    }

}
