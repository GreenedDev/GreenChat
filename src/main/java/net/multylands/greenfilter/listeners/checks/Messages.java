package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.utils.Chat;
import net.multylands.greenfilter.utils.ChecksUtils;
import net.multylands.greenfilter.utils.PunishmentUtils;
import net.multylands.greenfilter.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class Messages implements Listener {
    private GreenFilter plugin;

    public Messages(GreenFilter plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (GreenFilter.isChatEnabled != null) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("commands.toggle-chat.chat-is-disabled").replace("%player%", GreenFilter.isChatEnabled));
            event.setCancelled(true);
            return;
        }
        String all = Utils.replace(event.getMessage().toLowerCase());
        if (player.hasPermission("chat.bypass")) {
            GreenFilter.recentMessages.remove(player.getUniqueId());
            GreenFilter.recentMessages.put(player.getUniqueId(), all);
            return;
        }
        if (ChecksUtils.isRepeating(plugin, player, all)) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-repeat"));
            event.setCancelled(true);
            return;
        }
        boolean sworn = ChecksUtils.isSwearing(plugin, all);
        boolean advertised = ChecksUtils.isAdvertising(plugin, all);
        if (sworn || advertised) {
            event.setCancelled(true);
            PunishmentUtils.executePunishmentAsync(plugin, player, sworn, advertised, "chat", all);
            GreenFilter.recentMessages.remove(player.getUniqueId());
            GreenFilter.recentMessages.put(player.getUniqueId(), all);
            return;
        }

        if (ChecksUtils.isSpamming(plugin, player)) {
            //we are already sending warning message in the method
            event.setCancelled(true);
            return;
        }
        String noSpaceMessage = event.getMessage().replaceAll(" ", "");
        if (noSpaceMessage.length() >= 5) {
            if (ChecksUtils.isFlooding(plugin, noSpaceMessage)) {
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-flood"));
                event.setCancelled(true);
                GreenFilter.recentMessages.remove(player.getUniqueId());
                GreenFilter.recentMessages.put(player.getUniqueId(), all);
                return;
            }
            if (ChecksUtils.isYelling(plugin, noSpaceMessage)) {
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-caps"));
                event.setCancelled(true);
                GreenFilter.recentMessages.remove(player.getUniqueId());
                GreenFilter.recentMessages.put(player.getUniqueId(), all);
                return;
            }
        }
        GreenFilter.recentMessages.remove(player.getUniqueId());
        GreenFilter.recentMessages.put(player.getUniqueId(), all);
    }
}

