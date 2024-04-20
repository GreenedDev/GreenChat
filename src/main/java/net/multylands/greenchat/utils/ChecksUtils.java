package net.multylands.greenchat.utils;

import net.multylands.greenchat.GreenChat;
import org.bukkit.entity.Player;

public class ChecksUtils {
    public static boolean isSpamming(GreenChat plugin, Player player) {
        if (!plugin.configKeys.getOptionBoolean("anti-spam.enabled")) {
            return false;
        }
        if (!plugin.delay.containsKey(player)) {
            plugin.delay.put(player, System.currentTimeMillis());
            return false;
        } else {
            long secondsSinceLastMessage = (System.currentTimeMillis() - plugin.delay.get(player)) / 1000;
            if (secondsSinceLastMessage >= plugin.configKeys.getOptionInt("anti-spam.delay")) {
                plugin.delay.remove(player);
                return false;
            } else {
                String secondsLeft = String.valueOf(plugin.configKeys.getOptionInt("anti-spam.delay") - secondsSinceLastMessage);
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-spam.chat").replace("%seconds%", secondsLeft));
                return true;
            }
        }
    }
    public static boolean isSpammingCommand(GreenChat plugin, Player player) {
        if (!plugin.configKeys.getOptionBoolean("commands-anti-spam.enabled")) {
            return false;
        }
        if (!plugin.commandDelay.containsKey(player)) {
            plugin.commandDelay.put(player, System.currentTimeMillis());
            return false;
        } else {
            long secondsSinceLastMessage = (System.currentTimeMillis() - plugin.commandDelay.get(player)) / 1000;
            if (secondsSinceLastMessage >= plugin.configKeys.getOptionInt("commands-anti-spam.delay")) {
                plugin.commandDelay.remove(player);
                return false;
            } else {
                String secondsLeft = String.valueOf(plugin.configKeys.getOptionInt("commands-anti-spam.delay") - secondsSinceLastMessage);
                Chat.sendMessage(plugin, player, plugin.configKeys.getLang("warn.anti-spam.commands").replace("%seconds%", secondsLeft));
                return true;
            }
        }
    }
    public static boolean isSyntax(GreenChat plugin, String command) {
        if (!plugin.configKeys.getOptionBoolean("anti-syntax.enabled")) {
            return false;
        }
        if (!command.contains(" ")) {
            if (command.contains(":")) {
                return true;
            } else {
                return false;
            }
        }
        if (command.split(" ")[0].contains(":")) {
            return true;
        }
        return false;
    }
    public static boolean isFlooding(GreenChat plugin, String message) {
        if (!plugin.configKeys.getOptionBoolean("anti-flood.enabled")) {
            return false;
        }
        int searchingRadius = plugin.configKeys.getOptionInt("anti-flood.radius");
        for (int b = 0; b < message.length(); b++) {
            String charAt0OfMessage = String.valueOf(message.charAt(b));
            String charAt0TimesSearchingRadius = "";
            for (int i = 0; i < searchingRadius; i++) {
                charAt0TimesSearchingRadius = charAt0TimesSearchingRadius + charAt0OfMessage;

            }
            int lengthOfMessageAfterReplacedTheFirstCharWithRadius = message.replace(charAt0TimesSearchingRadius, "").length();
            if (message.length() != lengthOfMessageAfterReplacedTheFirstCharWithRadius) {
                System.out.println(charAt0OfMessage);
                return true;
            }
        }
        String lastLetters = message.substring(message.length() - searchingRadius);
        String charAt0OfLastLetters = String.valueOf(lastLetters.charAt(0));
        if (lastLetters.matches("[a-zA-Z]+")) {
            int numberOfLettersLeftAfterReplacingCharAt0 = lastLetters.replaceAll(charAt0OfLastLetters, "").length();
            return numberOfLettersLeftAfterReplacingCharAt0 == 0;
        } else {
            int FloodedChars = 0;
            for (int i = 0; i < lastLetters.length(); i++) {
                if (lastLetters.charAt(i) == '?' || lastLetters.charAt(i) == '!' || lastLetters.charAt(i) == '.') {
                    FloodedChars++;
                }
            }
            if (FloodedChars == searchingRadius) {
                return true;
            }
        }
        return false;
    }

    public static boolean isYelling(GreenChat plugin, String message) {
        if (!plugin.configKeys.getOptionBoolean("anti-caps.enabled")) {
            return false;
        }
        int upperCaseLetters = 0;
        for (int i = 0; i < message.length(); i++) {
            char charAt = message.charAt(i);
            if (!(charAt >= 'A' && charAt <= 'Z')) {
                continue;
            }
            upperCaseLetters++;
            if (upperCaseLetters < plugin.configKeys.getOptionInt("anti-caps.limit")) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static boolean isRepeating(GreenChat plugin, Player player, String text) {
        if (!plugin.configKeys.getOptionBoolean("anti-repeat.enabled")) {
            return false;
        }
        if (GreenChat.recentMessages.get(player.getUniqueId()) != null) {
            if (text.contains(GreenChat.recentMessages.get(player.getUniqueId()))) {
                if (GreenChat.recentMessages.get(player.getUniqueId()).length() > plugin.configKeys.getOptionInt("anti-repeat.size-barrier")) {
                    return true;
                }
                if (text.replaceAll("[!?.]", "").equals(GreenChat.recentMessages.get(player.getUniqueId()))) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isSwearing(GreenChat plugin, String all) {
        for (String blacklistedWord : plugin.getConfig().getStringList("anti-swear-words")) {
            if (!all.contains(blacklistedWord)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static boolean isAdvertising(GreenChat plugin, String all) {
        for (String blacklistedAd : plugin.getConfig().getStringList("anti-ad-blacklist")) {
            if (!all.contains(blacklistedAd)) {
                continue;
            }
            return true;
        }
        return false;
    }
}
