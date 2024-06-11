package net.multylands.greenfilter.utils;

import net.multylands.greenfilter.GreenFilter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.regex.qual.Regex;

public class ChecksUtils {
    public static boolean isSpamming(GreenFilter plugin, Player player) {
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
                Chat.sendMessage(player, plugin.configKeys.getLang("warn.anti-spam.chat").replace("%seconds%", secondsLeft));
                return true;
            }
        }
    }

    public static boolean isSpammingCommand(GreenFilter plugin, Player player) {
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
                Chat.sendMessage( player, plugin.configKeys.getLang("warn.anti-spam.commands").replace("%seconds%", secondsLeft));
                return true;
            }
        }
    }

    public static String isSyntax(GreenFilter plugin, String command) {
        if (!plugin.configKeys.getOptionBoolean("anti-syntax.enabled")) {
            return null;
        }
        if (!command.contains(" ")) {
            if (command.contains(":")) {
                return command;
            } else {
                return null;
            }
        }
        if (command.split(" ")[0].contains(":")) {
            return command.split(" ")[0];
        }
        return null;
    }

    public static String isFlooding(GreenFilter plugin, String message) {
        if (!plugin.configKeys.getOptionBoolean("anti-flood.enabled")) {
            return null;
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
                return charAt0TimesSearchingRadius;
            }
        }
        String lastLetters = message.substring(message.length() - searchingRadius);
        String charAt0OfLastLetters = String.valueOf(lastLetters.charAt(0));
        if (lastLetters.matches("[a-zA-Z]+")) {
            int numberOfLettersLeftAfterReplacingCharAt0 = lastLetters.replaceAll(charAt0OfLastLetters, "").length();
            if (numberOfLettersLeftAfterReplacingCharAt0 == 0) {
                return lastLetters;
            }
        } else {
            int FloodedChars = 0;
            for (int i = 0; i < lastLetters.length(); i++) {
                if (lastLetters.charAt(i) == '?' || lastLetters.charAt(i) == '!' || lastLetters.charAt(i) == '.') {
                    FloodedChars++;
                }
            }
            if (FloodedChars == searchingRadius) {
                return lastLetters;
            }
        }
        return null;
    }

    public static String getYellingPart(GreenFilter plugin, String message) {
        if (!plugin.configKeys.getOptionBoolean("anti-caps.enabled")) {
            return null;
        }
        int upperCaseLetters = 0;
        int starting = 0;
        for (int i = 0; i < message.length(); i++) {
            char charAt = message.charAt(i);
            if (charAt >= 'A' && charAt <= 'Z') {
                if (upperCaseLetters == 0) {
                    starting = i;
                }
                upperCaseLetters++;
                if (upperCaseLetters > plugin.configKeys.getOptionInt("anti-caps.limit")) {
                    String flaggedPart = message.substring(starting);
                    flaggedPart = flaggedPart.substring(0, i);
                    return flaggedPart;
                }
            } else {
                if (upperCaseLetters > plugin.configKeys.getOptionInt("anti-caps.limit")) {
                    String flaggedPart = message.substring(starting);
                    flaggedPart = flaggedPart.substring(0, i);
                    return flaggedPart;
                }
            }
        }
        return null;
    }

    public static String getRepeatingPart(GreenFilter plugin, Player player, String text) {
        if (!plugin.configKeys.getOptionBoolean("anti-repeat.enabled")) {
            return null;
        }
        if (GreenFilter.recentMessages.get(player.getUniqueId()) != null) {
            if (text.contains(GreenFilter.recentMessages.get(player.getUniqueId()))) {
                if (GreenFilter.recentMessages.get(player.getUniqueId()).length() > plugin.configKeys.getOptionInt("anti-repeat.size-barrier")) {
                    return text;
                }
                if (text.replaceAll("[!?.]", "").equals(GreenFilter.recentMessages.get(player.getUniqueId()))) {
                    return text;
                }
            }
        }
        return null;
    }

    public static String getSwearingPart(GreenFilter plugin, String all) {
        boolean regexEnabled = plugin.getConfig().getBoolean("regex-mode");
        if (regexEnabled) {
            for (String blacklistedWord : plugin.getConfig().getStringList("anti-swear-words")) {
                if (!all.matches(".*" + blacklistedWord + ".*")) {
                    continue;
                }
                return blacklistedWord;
            }
        } else {
            for (String blacklistedWord : plugin.getConfig().getStringList("anti-swear-words")) {
                if (!all.contains(blacklistedWord)) {
                    continue;
                }
                return blacklistedWord;
            }
        }
        return null;
    }

    public static String getAdvertisingPart(GreenFilter plugin, String all) {
        boolean regexEnabled = plugin.getConfig().getBoolean("regex-mode");
        if (regexEnabled) {
            for (String blacklistedAd : plugin.getConfig().getStringList("anti-advertising-blacklist")) {
                if (!all.matches(".*" + blacklistedAd + ".*")) {
                    continue;
                }
                return blacklistedAd;
            }
        } else {
            for (String blacklistedAd : plugin.getConfig().getStringList("anti-advertising-blacklist")) {
                if (!all.contains(blacklistedAd)) {
                    continue;
                }
                return blacklistedAd;
            }
        }
        return null;
    }
}
