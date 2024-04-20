package net.multylands.greenfilter.utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.multylands.greenfilter.GreenFilter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(GreenFilter plugin, Player player, String message) {
        if (message.startsWith("$")) {
            Component parsed = plugin.miniMessage().deserialize(message.substring(1));
            plugin.adventure().player(player).sendMessage(parsed);
        } else {
            player.sendMessage(color(message));
        }
    }

    public static void sendMessageSender(GreenFilter plugin, CommandSender sender, String message) {
        if (message.startsWith("$")) {
            Component parsed = plugin.miniMessage().deserialize(message.substring(1));
            plugin.adventure().sender(sender).sendMessage(parsed);
        } else {
            sender.sendMessage(color(message));
        }
    }
    public static void broadcast(GreenFilter plugin, String message) {
        if (message.startsWith("$")) {
            Component parsed = plugin.miniMessage().deserialize(message.substring(1));
            plugin.adventure().players().sendMessage(parsed);
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(color(message));
            }
        }
    }
}
