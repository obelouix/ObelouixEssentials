package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.commands.AdminCommand;
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

    protected final I18n i18n = I18n.getInstance();

    public abstract void showInventory(@NotNull Player player);

    @EventHandler(priority = EventPriority.NORMAL)
    public abstract void cancelClick(InventoryClickEvent event);

    @EventHandler(priority = EventPriority.NORMAL)
    public abstract void onInventoryClickEvent(InventoryClickEvent event);

    public void addBottomNavigationBar(@NotNull Inventory inventory) {
        //previous arrow
        inventory.setItem(48, HeadSkinFetcher.getOfflineSkull("Previous",
                Heads.LEFT_ARROW.toString()));

        //home
        inventory.setItem(49, AdminCommand.createGuiItem(new ItemStack(Material.BARRIER), Component.text("Home")
                .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));

        //next arrow
        inventory.setItem(50, HeadSkinFetcher.getOfflineSkull("Next",
                Heads.RIGHT_ARROW.toString()));
    }


}
