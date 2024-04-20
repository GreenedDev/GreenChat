package net.multylands.greenchat.commands;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand implements CommandExecutor, TabCompleter {
    public GreenChat plugin;

    public ChatCommand(GreenChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            for (String message : plugin.configKeys.getLangList("help")) {
                Chat.sendMessageSender(plugin, sender, message);
            }
            return false;
        }
        CommandExecutor executor = GreenChat.commandExecutors.get(args[0]);
        if (executor == null) {
            for (String message : plugin.configKeys.getLangList("help")) {
                Chat.sendMessageSender(plugin, sender, message);
            }
            return false;
        }
        executor.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabCompleteStrings = new ArrayList<>();
        for (String commands : GreenChat.commandExecutors.keySet()) {
            if (commands.startsWith(args[0])) {
                if (!commands.equalsIgnoreCase(args[0])) {
                    tabCompleteStrings.add(commands);
                }
            }
        }
        return tabCompleteStrings;
    }
}
