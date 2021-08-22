package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.utils.HeadSkinFetcher;
import fr.obelouix.essentials.utils.Heads;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class AdminGUI extends BaseGUI {

    private @NotNull Inventory admin(Player player) {
        final Inventory adminInventory = Bukkit.createInventory(null, 54, Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center")));

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        skullMeta.displayName(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center.player_management"))
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(255, 0, 0)));
        skull.setItemMeta(skullMeta);

        adminInventory.addItem(skull);
        adminInventory.setItem(4, HeadSkinFetcher.getOfflineSkull("World Management", Heads.GLOBE.toString()));
        return adminInventory;
    }

    @Override
    public void showInventory(@NotNull Player player) {
        player.openInventory(admin(player));
    }

    @Override
    @EventHandler
    public void cancelClick(@NotNull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().title().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center")))) {
            event.setCancelled(true);
        }
    }


    @Override
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();

        if (event.getView().title().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center")))) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType().equals(Material.PLAYER_HEAD)) {
                if (event.getSlot() == 0) {
                    new PlayerManagementGUI().showInventory(player);
                }
                if (event.getSlot() == 49 && clickedItem.displayName().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.home"))
                        .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false))) {
                    new AdminGUI().showInventory(player);
                }
            }
        }
    }
}
