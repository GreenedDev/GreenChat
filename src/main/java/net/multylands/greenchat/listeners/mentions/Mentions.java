package net.multylands.greenchat.listeners.mentions;

import net.multylands.greenchat.GreenChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Mentions implements Listener {
    private GreenChat plugin;

    public Mentions(GreenChat plugin) {
        this.plugin = plugin;
    }
    @EventHandler(ignoreCancelled = true)
    public void onPing(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
        if (!event.isAsynchronous()) {
            return;
        }
        String replacedMessage = ChatColor.stripColor(message.replace("&", "§"));
        replacedMessage = plugin.miniMessage().stripTags(replacedMessage);
        String[] words = replacedMessage.split(" ");
        for (String word : words) {
            Player pingedPlayer;
            if (word.length() <= 4) {
                pingedPlayer = Bukkit.getPlayerExact(word);
            } else {
                pingedPlayer = Bukkit.getPlayer(word);
            }
            if (pingedPlayer == null) {
                continue;
            }
            if (pingedPlayer.getName().equals(sender.getName())) {
                continue;
            }
            String pingedPlayerUUID = pingedPlayer.getUniqueId().toString();
            if (plugin.chatAlertsConfig.contains(pingedPlayerUUID)) {
                continue;
            }
            Sound pingSound = Sound.valueOf(plugin.configKeys.getOption("mentions.sound"));
            pingedPlayer.playSound(pingedPlayer.getLocation(), pingSound, 1, 2);
        }
    }
}
