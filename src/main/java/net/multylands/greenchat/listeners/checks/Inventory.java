package net.multylands.greenchat.listeners.checks;

import net.multylands.greenchat.GreenChat;
import net.multylands.greenchat.utils.ChecksUtils;
import net.multylands.greenchat.utils.PunishmentUtils;
import net.multylands.greenchat.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class Inventory implements Listener {
    private GreenChat plugin;

    public Inventory(GreenChat plugin) {
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
            boolean sworn = ChecksUtils.isSwearing(plugin, all);
            boolean advertised = ChecksUtils.isAdvertising(plugin, all);
            if (!(sworn || advertised)) {
                return;
            }
            event.setCancelled(true);
            event.getInventory().setItem(event.getSlot(), null);
            PunishmentUtils.executePunishment(plugin, player, sworn, advertised, "item", all);
        }
    }
}
