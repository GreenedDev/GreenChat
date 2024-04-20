package net.multylands.greenchat.commands.subcommands;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.objects.ConfigKeys;
import net.multylands.greenchat.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private GreenChat plugin;

    public ReloadCommand(GreenChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.reload")) {
            Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        plugin.reloadConfig();
        plugin.configKeys = new ConfigKeys(plugin);
        Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("reload"));
        return false;
    }
}
