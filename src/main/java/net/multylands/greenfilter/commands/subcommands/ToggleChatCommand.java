package net.multylands.greenfilter.commands.subcommands;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.utils.Chat;
import net.multylands.greenfilter.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleChatCommand implements CommandExecutor {
    private GreenFilter plugin;

    public ToggleChatCommand(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.toggle")) {
            Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        if (GreenFilter.isChatEnabled == null) {
            Chat.broadcast(plugin, Utils.replacePlayer(plugin.configKeys.getLang("commands.toggle-chat.someone-disabled-chat"), sender.getName()));
            GreenFilter.isChatEnabled = sender.getName();
        } else {
            Chat.broadcast(plugin, Utils.replacePlayer(plugin.configKeys.getLang("commands.toggle-chat.someone-enabled-chat"), sender.getName()));
            GreenFilter.isChatEnabled = null;
        }
        return false;

    }
}
