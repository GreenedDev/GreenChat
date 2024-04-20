package net.multylands.greenchat.listeners.checks;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import net.multylands.greenchat.utils.ChecksUtils;
import net.multylands.greenchat.utils.PunishmentUtils;
import net.multylands.greenchat.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Command implements Listener {
    private GreenChat plugin;

    public Command(GreenChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCmd(PlayerCommandPreprocessEvent event) {
        String all = Utils.replace(event.getMessage());
        Player player = event.getPlayer();
        String startCheck = all.replace("/", "");
        for (String whitelistedCommandStarting : plugin.getConfig().getStringList("options.commands-whitelist")) {
            if (startCheck.startsWith(whitelistedCommandStarting)) {
                return;
            }
        }
        if (player.hasPermission("chat.bypass")) {
            return;
        }
        if (ChecksUtils.isSyntax(plugin, all)) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.syntax"));
            event.setCancelled(true);
            return;
        }
        if (ChecksUtils.isSpammingCommand(plugin, player)) {
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-spam.commands"));
            event.setCancelled(true);
            return;
        }
        boolean sworn = ChecksUtils.isSwearing(plugin, all);
        boolean advertised = ChecksUtils.isAdvertising(plugin, all);
        if (!(sworn || advertised)) {
            return;
        }
        event.setCancelled(true);
        PunishmentUtils.executePunishmentAsync(plugin, player, sworn, advertised, "command", all);
    }
}
