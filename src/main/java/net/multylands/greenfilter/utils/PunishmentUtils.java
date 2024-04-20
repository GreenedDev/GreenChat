package net.multylands.greenfilter.utils;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PunishmentUtils {
    public static void executePunishment(GreenFilter plugin, Player player, CheckRule checkRule, Platform platform, String all, String word) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            executePunishmentCode(plugin, player, checkRule, platform, all, word);
        });
    }

    public static void executePunishmentAsync(GreenFilter plugin, Player player, CheckRule checkRule, Platform platform, String all, String word) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            executePunishmentCode(plugin, player, checkRule, platform, all, word);
        });
    }

    public static void executePunishmentCode(GreenFilter plugin, Player criminal, CheckRule checkRule, Platform platform, String all, String word) {
        String criminalName = criminal.getName();
        if (checkRule == CheckRule.sworn) {
            List<String> punishCommands = plugin.configKeys.getSwearingPunishCommands(platform);
            List<String> commandWithParsedPlaceholders = Utils.replacePlaceholdersList(punishCommands, criminalName, all);
            for (String command : commandWithParsedPlaceholders) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }

            if (!plugin.configKeys.isNotificationsEnabled(platform)) {
                return;
            }
            String swearFlagMessage = Utils.replacePlaceholders(plugin.configKeys.getSwearFlagMessage(platform), criminalName, all);
            swearFlagMessage = swearFlagMessage.replace(word, "&n" + word + "&7");
            sendFlagMessage(plugin, swearFlagMessage);
        } else if (checkRule == CheckRule.advertise) {
            List<String> punishCommands = plugin.configKeys.getAdvertisingPunishCommands(platform);
            List<String> commandWithParsedPlaceholders = Utils.replacePlaceholdersList(punishCommands, criminalName, all);
            for (String command : commandWithParsedPlaceholders) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }

            if (!plugin.configKeys.isNotificationsEnabled(platform)) {
                return;
            }
            String adFlagMessage = Utils.replacePlaceholders(plugin.configKeys.getAdFlagMessage(platform), criminalName, all);
            adFlagMessage = adFlagMessage.replace(word, "&n" + word + "&7");
            sendFlagMessage(plugin, adFlagMessage);
        }
    }

    public static void executeCheckRulePunishment(GreenFilter plugin, CheckRule checkRule, String criminalName, String all, String word) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            List<String> punishCommands = plugin.configKeys.getCheckRulePunishCommands(checkRule);
            List<String> commandWithParsedPlaceholders = Utils.replacePlaceholdersList(punishCommands, criminalName, all);
            for (String command : commandWithParsedPlaceholders) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            String notification = Utils.replacePlaceholders(plugin.configKeys.getNotificationMessage(checkRule), criminalName, all);
            if (checkRule != CheckRule.spam) {
                notification = notification.replace(word, "&n" + word + "&7");
            }
            sendFlagMessage(plugin, notification);
        });
    }

    public static void sendFlagMessage(GreenFilter plugin, String message) {
        for (Player user : Bukkit.getOnlinePlayers()) {
            if (!user.hasPermission("chat.notify")) {
                continue;
            }
            Chat.sendMessage(plugin, user, message);
        }
    }
}
