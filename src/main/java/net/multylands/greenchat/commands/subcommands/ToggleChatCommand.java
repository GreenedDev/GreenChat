package net.multylands.greenchat.commands.subcommands;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import net.multylands.greenchat.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleChatCommand implements CommandExecutor {
    private GreenChat plugin;

    public ToggleChatCommand(GreenChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chat.admin.toggle")) {
            Chat.sendMessageSender(plugin, sender, plugin.configKeys.getLang("no-perm"));
            return false;
        }
        if (GreenChat.isChatEnabled == null) {
            Chat.broadcast(plugin, Utils.replacePlayer(plugin.configKeys.getLang("commands.toggle-chat.someone-disabled-chat"), sender.getName()));
            GreenChat.isChatEnabled = sender.getName();
        } else {
            Chat.broadcast(plugin, Utils.replacePlayer(plugin.configKeys.getLang("commands.toggle-chat.someone-enabled-chat"), sender.getName()));
            GreenChat.isChatEnabled = null;
        }
        return false;

    }
}
