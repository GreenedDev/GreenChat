package net.multylands.greenchat.listeners.checks;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import net.multylands.greenchat.utils.ChecksUtils;
import net.multylands.greenchat.utils.PunishmentUtils;
import net.multylands.greenchat.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class Messages implements Listener {
    private GreenChat plugin;

    public Messages(GreenChat plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (GreenChat.isChatEnabled != null) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("commands.toggle-chat.chat-is-disabled").replace("%player%", GreenChat.isChatEnabled));
            event.setCancelled(true);
            return;
        }
        String all = Utils.replace(event.getMessage().toLowerCase());
        if (player.hasPermission("chat.bypass")) {
            GreenChat.recentMessages.remove(player.getUniqueId());
            GreenChat.recentMessages.put(player.getUniqueId(), all);
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
            GreenChat.recentMessages.remove(player.getUniqueId());
            GreenChat.recentMessages.put(player.getUniqueId(), all);
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
                GreenChat.recentMessages.remove(player.getUniqueId());
                GreenChat.recentMessages.put(player.getUniqueId(), all);
                return;
            }
            if (ChecksUtils.isYelling(plugin, noSpaceMessage)) {
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-caps"));
                event.setCancelled(true);
                GreenChat.recentMessages.remove(player.getUniqueId());
                GreenChat.recentMessages.put(player.getUniqueId(), all);
                return;
            }
        }
        GreenChat.recentMessages.remove(player.getUniqueId());
        GreenChat.recentMessages.put(player.getUniqueId(), all);
    }
}

