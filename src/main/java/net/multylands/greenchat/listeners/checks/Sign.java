package net.multylands.greenchat.listeners.checks;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.ChecksUtils;
import net.multylands.greenchat.utils.PunishmentUtils;
import net.multylands.greenchat.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class Sign implements Listener {
    private GreenChat plugin;

    public Sign(GreenChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSign(SignChangeEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();
            StringBuilder lines = new StringBuilder();
            if (player.hasPermission("chat.bypass")) {
                return;
            }
            for (int i = 0; i < 4; i++) {
                lines.append(event.getLine(i));
            }
            lines = new StringBuilder(Utils.replace(lines.toString().toLowerCase().replaceAll(" ", "")));
            String all = lines.toString().replace("textcomponentimplcontent,stylestyleimplcolornull,obfuscatednotset,boldnotset,strikethroughnotset,underlinednotset,italicnotset,clickeventnull,hovereventnull,insertionnull,fontnull,children", "");
            if (all.isEmpty()) {
                return;
            }
            boolean sworn = ChecksUtils.isSwearing(plugin, all);
            boolean advertised = ChecksUtils.isAdvertising(plugin, all);
            if (!(sworn || advertised)) {
                return;
            }
            event.getBlock().breakNaturally();
            PunishmentUtils.executePunishment(plugin, player, sworn, advertised, "sign", all);
        });
    }
}
