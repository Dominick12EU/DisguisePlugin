package it.dominick.dp.config;

import it.dominick.dp.utils.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Messages {

    PREFIX("general.prefix"),
    DISABLE_DISGUISE("disguise.disable"),
    ENABLE_DISGUISE("disguise.enable"),
    JOIN_ENABLE_DISGUISE("disguise.enableOnJoin"),
    LIST_DISGUISE("disguise.list"),
    LIST_FORMAT_DISGUISE("disguise.list_format"),
    ERROR_DISGUISE("disguise.skinNotFound"),
    ERROR_EMPTY_LIST("disguise.emptyList");

    private static FileConfiguration config;
    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public static void setConfiguration(FileConfiguration c) {
        config = c;
    }

    public void send(CommandSender receiver, Object... replacements) {
        Object value = config.get("Messages." + this.path);

        String message;
        if (value == null) {
            message = "Messaggio non trovato (" + this.path + ")";
        } else {
            message = value instanceof List ? ChatUtil.fromList((List<?>) value) : value.toString();
        }

        if (!message.isEmpty()) {
            receiver.sendMessage(ChatUtil.color(replace(message, replacements)));
        }
    }

    private String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 >= replacements.length) break;
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        String prefix = config.getString("Messages." + PREFIX.getPath());
        return message.replace("%prefix%", prefix != null && !prefix.isEmpty() ? prefix : "");
    }

    public String getPath() {
        return this.path;
    }

}
