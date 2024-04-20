package net.multylands.greenchat.listeners.checks;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.ChecksUtils;
import net.multylands.greenchat.utils.PunishmentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    private GreenChat plugin;

    public Join(GreenChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String all = event.getPlayer().getName().toLowerCase().replaceAll("[^A-Za-z ]", "");
        boolean sworn = ChecksUtils.isSwearing(plugin, all);
        boolean advertised = ChecksUtils.isAdvertising(plugin, all);
        if (!(sworn || advertised)) {
            return;
        }
        event.setJoinMessage(null);
        PunishmentUtils.executePunishmentAsync(plugin, player, sworn, advertised, "username", all);
    }
}
