package net.multylands.greenchat.utils;

import net.multylands.greenchat.GreenChat;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;

public class ServerUtils {
    public static void implementBStats(GreenChat plugin) {
        Metrics metrics = new Metrics(plugin, 21659);
        metrics.addCustomChart(new SingleLineChart("servers", () -> {
            return 1;
        }));
    }
    public static void checkForUpdates(GreenChat plugin) {
        new UpdateChecker(plugin, 114685).getVersion(version -> {
            if (!plugin.getDescription().getVersion().equals(version)) {
                GreenChat.newVersion = version;
                Chat.sendMessageSender(plugin, Bukkit.getConsoleSender(), plugin.configKeys.getLang("update-available").replace("%newversion%", version));
            }
        });
    }
}
