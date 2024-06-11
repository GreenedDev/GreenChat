package net.multylands.greenfilter;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.multylands.greenfilter.commands.GreenFilterCommand;
import net.multylands.greenfilter.commands.subcommands.ClearChatCommand;
import net.multylands.greenfilter.commands.subcommands.ToggleAlertsCommand;
import net.multylands.greenfilter.commands.subcommands.ReloadCommand;
import net.multylands.greenfilter.commands.subcommands.ToggleChatCommand;
import net.multylands.greenfilter.listeners.checks.*;
import net.multylands.greenfilter.listeners.mentions.Mentions;
import net.multylands.greenfilter.objects.ConfigKeys;
import net.multylands.greenfilter.utils.ConfigUtils;
import net.multylands.greenfilter.utils.ServerUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class GreenFilter extends JavaPlugin implements Listener {
    public HashMap<Player, Long> delay = new HashMap<>();
    public static HashMap<UUID, String> recentMessages = new HashMap<>();

    public static HashMap<String, CommandExecutor> commandExecutors = new HashMap<>();

    public HashMap<Player, Long> commandDelay = new HashMap<>();
    public static String newVersion = null;
    public File dir = getDataFolder();
    public File ChatAlertsFile = new File(dir, "ChatAlerts.yml");
    public FileConfiguration chatAlertsConfig = YamlConfiguration.loadConfiguration(ChatAlertsFile);
    public ConfigKeys configKeys;

    public static MiniMessage miniMessage;

    public static String isChatEnabled = null;

    @Override
    public void onEnable() {
        miniMessage = MiniMessage.miniMessage();
        configKeys = new ConfigKeys(this);
        ConfigUtils keys = new ConfigUtils(this);
        keys.addMissingKeysAndValues(getConfig(), "config.yml");
        ServerUtils.checkForUpdates(this);
        ServerUtils.implementBStats(this);
        getServer().getPluginManager().registerEvents(new Book(this), this);
        getServer().getPluginManager().registerEvents(new Messages(this), this);
        getServer().getPluginManager().registerEvents(new Command(this), this);
        getServer().getPluginManager().registerEvents(new Inventory(this), this);
        getServer().getPluginManager().registerEvents(new Join(this), this);
        getServer().getPluginManager().registerEvents(new Sign(this), this);
        getServer().getPluginManager().registerEvents(new Mentions(this), this);

        getCommand("chat").setExecutor(new GreenFilterCommand(this));
        commandExecutors.put("reload", new ReloadCommand(this));
        if (configKeys.getOptionBoolean("commands.alerts")) {
            commandExecutors.put("alerts", new ToggleAlertsCommand(this));
        }
        if (configKeys.getOptionBoolean("commands.clear-chat")) {
            commandExecutors.put("clear", new ClearChatCommand(this));
        }
        if (configKeys.getOptionBoolean("commands.toggle-chat")) {
            commandExecutors.put("toggle", new ToggleChatCommand(this));
        }

        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!ChatAlertsFile.exists()) {
            try {
                ChatAlertsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        getLogger().info("Chat Enabled");
        getLogger().info("Hi!");
    }

    public void saveChatAlertsConfig() {
        try {
            chatAlertsConfig.save(ChatAlertsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatAlertsConfig = YamlConfiguration.loadConfiguration(ChatAlertsFile);

    }

    @Override
    public void onDisable() {
        getLogger().info("Chat Disabled");
        getLogger().info("Goodbye...");
    }
}
