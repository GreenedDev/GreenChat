package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
import net.multylands.greenfilter.utils.ChecksUtils;
import net.multylands.greenfilter.utils.PunishmentUtils;
import net.multylands.greenfilter.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

public class Book implements Listener {
    private GreenFilter plugin;

    public Book(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBook(PlayerEditBookEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();
            if (player.hasPermission("chat.bypass")) {
                return;
            }
            String all = Utils.replace(event.getNewBookMeta().getPages().toString());
            boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
            boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
            if (!(sworn || advertised)) {
                return;
            }
            if (player.getInventory().getItemInMainHand().getType() == Material.WRITABLE_BOOK || player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) {
                player.getInventory().setItemInMainHand(null);
            } else if (player.getInventory().getItemInOffHand().getType() == Material.WRITABLE_BOOK || player.getInventory().getItemInOffHand().getType() == Material.WRITTEN_BOOK) {
                player.getInventory().setItemInOffHand(null);
            }
            event.setCancelled(true);
            if (sworn) {
                PunishmentUtils.executePunishment(plugin, player, CheckRule.sworn, Platform.book, all, ChecksUtils.getSwearingPart(plugin, all));
            }
            if (advertised) {
                PunishmentUtils.executePunishment(plugin, player, CheckRule.advertise, Platform.book, all, ChecksUtils.getAdvertisingPart(plugin, all));
            }
        });
    }
}