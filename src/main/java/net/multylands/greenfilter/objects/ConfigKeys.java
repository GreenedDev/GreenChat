package net.multylands.greenfilter.objects;

import net.multylands.greenfilter.GreenFilter;

import java.util.ArrayList;
import java.util.List;

public class ConfigKeys {
    GreenFilter plugin;

    public ConfigKeys(GreenFilter plugin) {
        this.plugin = plugin;
    }

    public List<String> replaceSlash(List<String> listOfCommandsWithSlash) {
        List<String> newList = new ArrayList<>();
        for (String command : listOfCommandsWithSlash) {
            newList.add(command.replace("/", ""));
        }
        return newList;
    }

    public List<String> getSwearingPunishCommands(Platform platform) {
        return replaceSlash(plugin.getConfig().getStringList("punishment." + platform + ".commands.swearing"));
    }

    public List<String> getAdvertisingPunishCommands(Platform platform) {
        return replaceSlash(plugin.getConfig().getStringList("punishment." + platform + ".commands.advertising"));
    }
    public List<String> getCheckRulePunishCommands(CheckRule checkRule) {
        return replaceSlash(plugin.getConfig().getStringList("punishment." + checkRule + ".commands"));
    }
    public String getSwearFlagMessage(Platform platform) {
        return plugin.getConfig().getString("punishment." + platform + ".notifications.swearing-message");
    }

    public String getAdFlagMessage(Platform platform) {
        return plugin.getConfig().getString("punishment." + platform + ".notifications.advertising-message");
    }
    public boolean isNotificationsEnabled(Platform platform) {
        return plugin.getConfig().getBoolean("punishment."+platform+".notifications.enabled");
    }

    public String getOption(String key) {
        return plugin.getConfig().getString("options." + key);
    }
    public String getNotificationMessage(CheckRule checkRule) {
        return plugin.getConfig().getString("punishment."+checkRule+".notifications.message");
    }
    public int getOptionInt(String key) {
        return plugin.getConfig().getInt("options." + key);
    }

    public boolean getOptionBoolean(String key) {
        return plugin.getConfig().getBoolean("options." + key);
    }

    public String getLang(String key) {
        return plugin.getConfig().getString("language." + key);
    }
    public List<String> getLangList(String key) {
        return plugin.getConfig().getStringList("language." + key);
    }
}
