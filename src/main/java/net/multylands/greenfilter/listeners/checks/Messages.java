package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
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
        String criminalName = player.getName();
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
        boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
        boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
        if (sworn || advertised) {
            event.setCancelled(true);
            if (sworn) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.sworn, Platform.chat, all, ChecksUtils.getSwearingPart(plugin, all));
            }
            if (advertised) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.advertise, Platform.chat, all, ChecksUtils.getAdvertisingPart(plugin, all));
            }
            return;
        }

        if (ChecksUtils.getRepeatingPart(plugin, player, all) != null) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-repeat"));
            PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.repeat, criminalName, all, ChecksUtils.getRepeatingPart(plugin, player, all));
            event.setCancelled(true);
            return;
        }

        if (ChecksUtils.isSpamming(plugin, player)) {
            //we are already sending warning message in the method
            PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.spam, criminalName, all, null);
            event.setCancelled(true);
            return;
        }
        String noSpaceMessage = event.getMessage().replaceAll(" ", "");
        if (noSpaceMessage.length() >= 5) {
            if (ChecksUtils.isFlooding(plugin, noSpaceMessage) != null) {
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-flood"));
                PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.flood, criminalName, all, ChecksUtils.isFlooding(plugin, noSpaceMessage));
                event.setCancelled(true);
                return;
            }
            if (ChecksUtils.getYellingPart(plugin, noSpaceMessage) != null) {
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-caps"));
                PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.caps, criminalName, event.getMessage(), ChecksUtils.getYellingPart(plugin, noSpaceMessage));
                event.setCancelled(true);
                return;
            }
        }
        GreenFilter.recentMessages.remove(player.getUniqueId());
        GreenFilter.recentMessages.put(player.getUniqueId(), all);
    }
}

