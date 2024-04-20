package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
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
            boolean sworn = ChecksUtils.isSwearing(plugin, all);
            boolean advertised = ChecksUtils.isAdvertising(plugin, all);
            if (!(sworn || advertised)) {
                return;
            }
            if (player.getInventory().getItemInMainHand().getType() == Material.WRITABLE_BOOK || player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) {
                player.getInventory().setItemInMainHand(null);
            } else if (player.getInventory().getItemInOffHand().getType() == Material.WRITABLE_BOOK || player.getInventory().getItemInOffHand().getType() == Material.WRITTEN_BOOK) {
                player.getInventory().setItemInOffHand(null);
            }
            event.setCancelled(true);
            PunishmentUtils.executePunishment(plugin, player, sworn, advertised, "book", all);
        });
    }
}