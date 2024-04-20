package net.multylands.greenchat.objects;

import net.multylands.greenchat.GreenChat;

import java.util.List;

public class ConfigKeys {
    GreenChat plugin;

    public ConfigKeys(GreenChat plugin) {
        this.plugin = plugin;
    }

    public String replaceSlash(String command) {
        return command.replace("/", "");
    }

    public String getSwearingPunishCommand(String platform) {
        return replaceSlash(plugin.getConfig().getString("punish-commands." + platform + ".swearing"));
    }

    public String getAdvertisingPunishCommand(String platform) {
        return replaceSlash(plugin.getConfig().getString("punish-commands." + platform + ".advertising"));
    }

    public String getSwearingFlaggedMessage(String platform) {
        return replaceSlash(plugin.getConfig().getString("punish-commands." + platform + ".swearing-message"));
    }

    public String getAdvertisingFlaggedMessage(String platform) {
        return replaceSlash(plugin.getConfig().getString("punish-commands." + platform + ".advertising-message"));
    }

    public String getOption(String key) {
        return plugin.getConfig().getString("options." + key);
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
