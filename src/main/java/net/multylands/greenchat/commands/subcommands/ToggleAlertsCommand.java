package net.multylands.greenchat.commands.subcommands;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleAlertsCommand implements CommandExecutor {
    private GreenChat plugin;

    public ToggleAlertsCommand(GreenChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission("chat.mentions.toggle")) {
            Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        Player player = (Player) sender;
        String playerUUID = player.getUniqueId().toString();
        if (!plugin.chatAlertsConfig.contains(playerUUID)) {
            plugin.chatAlertsConfig.set(playerUUID, true);
            plugin.saveChatAlertsConfig();
            Chat.sendMessage(plugin, player, plugin.configKeys.getLang("commands.toggle-alerts.enable"));
            return false;
        }
        plugin.chatAlertsConfig.set(playerUUID, null);
        plugin.saveChatAlertsConfig();
        Chat.sendMessage(plugin, player, plugin.configKeys.getLang("commands.toggle-alerts.disable"));
        return false;

    }
}
