package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
import net.multylands.greenfilter.utils.ChecksUtils;
import net.multylands.greenfilter.utils.PunishmentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    private GreenFilter plugin;

    public Join(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String all = event.getPlayer().getName().toLowerCase().replaceAll("[^A-Za-z ]", "");
        boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
        boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
        if (!(sworn || advertised)) {
            return;
        }
        event.setJoinMessage(null);
        if (sworn) {
            PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.sworn, Platform.username, all, ChecksUtils.getSwearingPart(plugin, all));
        }
        if (advertised) {
            PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.advertise, Platform.username, all, ChecksUtils.getAdvertisingPart(plugin, all));
        }
    }
}
