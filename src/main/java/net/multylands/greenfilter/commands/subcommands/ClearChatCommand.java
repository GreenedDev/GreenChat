package net.multylands.greenfilter.commands.subcommands;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
    GreenFilter plugin;

    public ClearChatCommand(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.clear")) {
            Chat.sendMessageSender(sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        for (int i = 0; i < 600; i++) {
            Chat.broadcast("");
        }
        if (sender instanceof Player) {
            Player executor = (Player) sender;
            Chat.broadcast(plugin.configKeys.getLang("commands.clear-chat.player").replace("%player%", executor.getName()));
            return false;
        }
        Chat.broadcast(plugin.configKeys.getLang("commands.clear-chat.console"));
        return false;
    }
}
