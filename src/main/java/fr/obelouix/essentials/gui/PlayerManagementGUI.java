package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.commands.AdminCommand;
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
import org.jetbrains.annotations.NotNull;

public class PlayerManagementGUI extends BaseGUI {
    private int pageNumber = 0;

    private @NotNull Inventory PlayerManagementInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_center.player_management")));
        addBottomNavigationBar(inventory, player);
        int slot = 0;

        for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
            Player p = (Player) Bukkit.getOnlinePlayers().toArray()[i + (44 * pageNumber)];
            inventory.setItem(slot, HeadSkinFetcher.getSkull(p));
            if (i == Bukkit.getOnlinePlayers().size()) {
                break;
            }
            slot++;
            if (slot == 44 && pageNumber < (Bukkit.getOnlinePlayers().size() / 44) + 1) {
                slot = 0;
                pageNumber += 1;
            }
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

    @Override
    public void addBottomNavigationBar(@NotNull Inventory inventory, Player player) {

        if (Bukkit.getOnlinePlayers().size() < 44) {
            //home
            inventory.setItem(49, AdminCommand.createGuiItem(new ItemStack(Material.BARRIER), Component.text(i18n.sendTranslatedMessage(player, "obelouix.home"))
                    .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));
        } else {
            if (pageNumber == 0) {

                //home
                inventory.setItem(49, AdminCommand.createGuiItem(new ItemStack(Material.BARRIER), Component.text(i18n.sendTranslatedMessage(player, "obelouix.home"))
                        .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));

                //next arrow
                inventory.setItem(50, HeadSkinFetcher.getOfflineSkull(i18n.sendTranslatedMessage(player, "obelouix.next_page"),
                        Heads.RIGHT_ARROW.toString()));
            } else if (pageNumber == Bukkit.getOnlinePlayers().size() / 44) {

                //previous arrow
                inventory.setItem(48, HeadSkinFetcher.getOfflineSkull(i18n.sendTranslatedMessage(player, "obelouix.previous_page"),
                        Heads.LEFT_ARROW.toString()));

                //home
                inventory.setItem(49, AdminCommand.createGuiItem(new ItemStack(Material.BARRIER), Component.text(i18n.sendTranslatedMessage(player, "obelouix.home"))
                        .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));

            } else super.addBottomNavigationBar(inventory, player);
        }
    }
}
