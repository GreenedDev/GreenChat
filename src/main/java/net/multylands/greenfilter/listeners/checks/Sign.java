package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
import net.multylands.greenfilter.utils.ChecksUtils;
import net.multylands.greenfilter.utils.PunishmentUtils;
import net.multylands.greenfilter.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class Sign implements Listener {
    private GreenFilter plugin;

    public Sign(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSign(SignChangeEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> {
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
            boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
            boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
            if (!(sworn || advertised)) {
                return;
            }
            event.getBlock().breakNaturally();
            if (sworn) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.sworn, Platform.sign, all, ChecksUtils.getSwearingPart(plugin, all));
            }
            if (advertised) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.advertise, Platform.sign, all, ChecksUtils.getAdvertisingPart(plugin, all));
            }
        });
    }
}
