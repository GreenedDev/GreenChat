package net.multylands.greenchat.commands.subcommands;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
    GreenChat plugin;

    public ClearChatCommand(GreenChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.clear")) {
            Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        for (int i = 0; i < 600; i++) {
            Chat.broadcast(plugin, "");
        }
        if (sender instanceof Player) {
            Player executor = (Player) sender;
            Chat.broadcast(plugin, plugin.configKeys.getLang("commands.clear-chat.player").replace("%player%", executor.getName()));
            return false;
        }
        Chat.broadcast(plugin, plugin.configKeys.getLang("commands.clear-chat.console"));
        return false;
    }
}
