package net.multylands.greenfilter.utils;

import net.multylands.greenfilter.GreenFilter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PunishmentUtils {
    public static void executePunishment(GreenFilter plugin, Player player, boolean sworn, boolean advertised, String platform, String all) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            executePunishmentCode(plugin, player, sworn, advertised, platform, all);
        });
    }

    public static void executePunishmentAsync(GreenFilter plugin, Player player, boolean sworn, boolean advertised, String platform, String all) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            executePunishmentCode(plugin, player, sworn, advertised, platform, all);
        });
    }

    public static void executePunishmentCode(GreenFilter plugin, Player player, boolean sworn, boolean advertised, String platform, String all) {
        if (sworn) {
            String command = plugin.configKeys.getSwearingPunishCommand(platform);
            String commandWithParsedPlaceholders = Utils.replacePlaceholders(command, player.getName(), all);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandWithParsedPlaceholders);
        } else if (advertised) {
            String command = plugin.configKeys.getAdvertisingPunishCommand(platform);
            String commandWithParsedPlaceholders = Utils.replacePlaceholders(command, player.getName(), all);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandWithParsedPlaceholders);
        }
        for (Player user : Bukkit.getOnlinePlayers()) {
            if (!user.hasPermission("chat.notify")) {
                continue;
            }
            if (sworn) {
                Chat.sendMessage(plugin, user, plugin.configKeys.getSwearingFlaggedMessage(platform));
            } else if (advertised) {
                Chat.sendMessage(plugin, user, plugin.configKeys.getAdvertisingFlaggedMessage(platform));
            }
        }
    }
}
