package net.multylands.greenfilter.utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.multylands.greenfilter.GreenFilter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Chat {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(Player player, String message) {
        if (message.startsWith("$")) {
            Component parsed = ServerUtils.miniMessage().deserialize(message.substring(1));
            player.sendMessage(parsed);
        } else {
            player.sendMessage(color(message));
        }
    }

    public static void sendMessageSender(CommandSender sender, String message) {
        if (message.startsWith("$")) {
            Component parsed = ServerUtils.miniMessage().deserialize(message.substring(1));
            sender.sendMessage(parsed);
        } else {
            sender.sendMessage(color(message));
        }
    }
    public static void broadcast(String message) {
        if (message.startsWith("$")) {
            Component parsed = ServerUtils.miniMessage().deserialize(message.substring(1));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(parsed);
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(color(message));
            }
        }
    }
}
