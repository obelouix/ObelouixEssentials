package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.utils.HeadSkinFetcher;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class PlayerManagementGUI extends BaseGUI {

    private @NotNull Inventory PlayerManagementInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center.player_management")));
        addBottomNavigationBar(inventory);
        int slot = 0;
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            inventory.setItem(slot, HeadSkinFetcher.getSkull(onlinePlayers));
            if (slot < 45) slot++;
        }
        return inventory;
    }


    @Override
    public void showInventory(@NotNull Player player) {
        player.openInventory(PlayerManagementInventory(player));
    }

    @Override
    @EventHandler
    public void cancelClick(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().title().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center.player_management")))) {
            event.setCancelled(true);
        }
    }

    @Override
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {

    }
}
