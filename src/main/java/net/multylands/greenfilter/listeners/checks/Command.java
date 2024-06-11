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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Command implements Listener {
    private GreenFilter plugin;

    public Command(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCmd(PlayerCommandPreprocessEvent event) {
        String all = Utils.replace(event.getMessage());
        Player player = event.getPlayer();
        String criminalName = player.getName();
        String startCheck = all.replace("/", "");
        for (String whitelistedCommandStarting : plugin.getConfig().getStringList("options.commands-whitelist")) {
            if (startCheck.startsWith(whitelistedCommandStarting)) {
                return;
            }
        }
        if (player.hasPermission("chat.bypass")) {
            return;
        }
        if (ChecksUtils.isSyntax(plugin, all) != null) {
            Chat.sendMessage(player, plugin.configKeys.getLang("warn.syntax"));
            event.setCancelled(true);
            PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.syntax, criminalName, all, ChecksUtils.isSyntax(plugin, all));
            return;
        }
        if (ChecksUtils.isSpammingCommand(plugin, player)) {
            Chat.sendMessage( player, plugin.configKeys.getLang("warn.anti-spam.commands"));
            event.setCancelled(true);
            PunishmentUtils.executeCheckRulePunishment(plugin, CheckRule.spam, criminalName, all, null);
            return;
        }
        boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
        boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
        if (!(sworn || advertised)) {
            return;
        }
        event.setCancelled(true);
        if (sworn) {
            PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.sworn, Platform.command, all, ChecksUtils.getSwearingPart(plugin, all));
        }
        if (advertised) {
            PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.advertise, Platform.command, all, ChecksUtils.getAdvertisingPart(plugin, all));
        }
    }
}
