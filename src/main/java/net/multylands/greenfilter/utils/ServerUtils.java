package net.multylands.greenfilter.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.multylands.greenfilter.GreenFilter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;

public class ServerUtils {
    public static void implementBStats(GreenFilter plugin) {
        Metrics metrics = new Metrics(plugin, 21662);
        metrics.addCustomChart(new SingleLineChart("servers", () -> {
            return 1;
        }));
    }
    public static void checkForUpdates(GreenFilter plugin) {
        new UpdateChecker(plugin, 116285).getVersion(version -> {
            if (!plugin.getDescription().getVersion().equals(version)) {
                GreenFilter.newVersion = version;
                Chat.sendMessageSender(Bukkit.getConsoleSender(), plugin.configKeys.getLang("update-available").replace("%newversion%", version));
            }
        });
    }
    public static MiniMessage miniMessage() {
        if (GreenFilter.miniMessage == null) {
            throw new IllegalStateException("miniMessage is null when getting it from the main class");
        }
        return GreenFilter.miniMessage;
    }
}
