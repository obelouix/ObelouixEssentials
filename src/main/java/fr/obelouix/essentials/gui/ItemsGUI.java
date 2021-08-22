package fr.obelouix.essentials.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemsGUI {

    /**
     * Create an item for custom inventories with a custom name and lore
     *
     * @param material  the material {@link Material}
     * @param component The name of the Object {@link Component}
     * @param lore      The tooltip {@link Component}
     * @return the custom item
     */
    public static ItemStack createGuiItem(@NotNull final ItemStack material, @NotNull final Component component, @Nullable final List<Component> lore) {

        // Create a new item
        final ItemStack item = new ItemStack(material.getType(), 1);

        // Get the item's meta
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.displayName(component);

        // Set the lore of the item
        meta.lore(lore);

        // Apply the display name and the lore to the item
        item.setItemMeta(meta);

        return item;
    }

}
