package net.multylands.greenfilter.listeners.checks;

import net.multylands.greenfilter.GreenFilter;
import net.multylands.greenfilter.objects.CheckRule;
import net.multylands.greenfilter.objects.Platform;
import net.multylands.greenfilter.utils.ChecksUtils;
import net.multylands.greenfilter.utils.PunishmentUtils;
import net.multylands.greenfilter.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class Inventory implements Listener {
    private GreenFilter plugin;

    public Inventory(GreenFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItem(InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission("chat.bypass")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
            String all = Utils.replace(event.getCurrentItem().getItemMeta().getDisplayName());
            boolean sworn = ChecksUtils.getSwearingPart(plugin, all) != null;
            boolean advertised = ChecksUtils.getAdvertisingPart(plugin, all) != null;
            if (!(sworn || advertised)) {
                return;
            }
            event.setCancelled(true);
            event.getInventory().setItem(event.getSlot(), null);
            if (sworn) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.sworn, Platform.item, all, ChecksUtils.getSwearingPart(plugin, all));
            }
            if (advertised) {
                PunishmentUtils.executePunishmentAsync(plugin, player, CheckRule.advertise, Platform.item, all, ChecksUtils.getAdvertisingPart(plugin, all));
            }
        }
    }
}
