package net.multylands.greenfilter.commands.subcommands;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.ConfigKeys;
import net.multylands.greenfilter.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private GreenFilter plugin;

    public ReloadCommand(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.reload")) {
            Chat.sendMessageSender(sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        plugin.reloadConfig();
        plugin.configKeys = new ConfigKeys(plugin);
        Chat.sendMessageSender(sender, plugin.configKeys.getLang("reload"));
        return false;
    }
}
