package net.multylands.greenfilter.listeners.update;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Update implements Listener {
    GreenFilter plugin;

    public Update(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (plugin.newVersion == null) {
            return;
        }
        Chat.sendMessage(plugin, player, plugin.configKeys.getLang("update-available").replace("%newversion%", plugin.newVersion));
    }
}
