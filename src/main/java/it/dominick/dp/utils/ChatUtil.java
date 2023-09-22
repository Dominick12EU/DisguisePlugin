package it.dominick.dp.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class ChatUtil {

    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String fromList(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if(org.bukkit.ChatColor.stripColor(list.get(i).toString()).equals("")) builder.append("\n&r");
            else builder.append(list.get(i).toString()).append(i + 1 != list.size() ? "\n" : "");
        }

        return builder.toString();
    }
}
