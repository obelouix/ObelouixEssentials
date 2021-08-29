package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.utils.HeadSkinFetcher;
import fr.obelouix.essentials.utils.Heads;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGUI implements Listener {

    protected final Essentials plugin = Essentials.getInstance();
    protected final I18n i18n = I18n.getInstance();

    /**
     * Used to show a custom inventory to a player
     *
     * @param player The player who will see the custom inventory
     */
    public abstract void showInventory(@NotNull Player player);

    /**
     * Used to cancel the click event to avoid player stealing items in the
     * custom inventory
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public abstract void cancelClick(InventoryClickEvent event);

    /**
     * Control what to do on each clicked objects
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public abstract void onInventoryClickEvent(InventoryClickEvent event);

    /**
     * Add a navigation bar at the bottom of an inventory
     *
     * @param inventory the inventory were we add the navigation bar
     * @param player    the player that will see the inventory (needed to translate each objects in the player locale)
     */
    public void addBottomNavigationBar(@NotNull Inventory inventory, Player player) {
        //previous arrow
        inventory.setItem(48, HeadSkinFetcher.getOfflineSkull(i18n.getTranslation(player, "obelouix.previous_page"),
                Heads.LEFT_ARROW.toString()));

        //home
        inventory.setItem(49, ItemsGUI.createGuiItem(new ItemStack(Material.BARRIER), Component.text(i18n.getTranslation(player, "obelouix.home"))
                .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));

        //next arrow
        inventory.setItem(50, HeadSkinFetcher.getOfflineSkull(i18n.getTranslation(player, "obelouix.next_page"),
                Heads.RIGHT_ARROW.toString()));
    }


}
