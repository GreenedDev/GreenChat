package net.multylands.greenchat.listeners.update;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Update implements Listener {
    GreenChat plugin;

    public Update(GreenChat plugin) {
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
