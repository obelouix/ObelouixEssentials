package fr.obelouix.essentials.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem {

    /**
     * allows to add an {@link Enchantment}
     *
     * @param item         the item that will receive the enchantment
     * @param enchantment  the {@link Enchantment} to add
     * @param enchantLevel an {@link Integer} between {@code 0} and {@link 255}
     */
    public static void addEnchantment(ItemStack item, Enchantment enchantment, int enchantLevel) {
        final ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addEnchant(enchantment, enchantLevel, true);
        item.setItemMeta(meta);
    }

    /**
     * allows to add multiple {@link Enchantment} at once
     *
     * @param item         the item that will receive the enchantments
     * @param enchantments a {@link List} of {@link Enchantment}
     * @param enchantLevel an {@link Integer} between {@code 0} and {@link 255}
     */
    public static void addEnchantments(ItemStack item, List<Enchantment> enchantments, int enchantLevel) {
        final ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        for (final Enchantment enchantment : enchantments) {
            meta.addEnchant(enchantment, enchantLevel, true);
        }
        item.setItemMeta(meta);
    }

    /**
     * change the name of the item
     *
     * @param item     the item were we change the name
     * @param itemName a {@link Component} that will be the name if this item
     */
    public static void setItemName(ItemStack item, Component itemName) {
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(itemName.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
    }

    /**
     * add lore (aka description) to the item
     *
     * @param item       the item were whe add the lore
     * @param components a {@link List} of {@link Component} to show
     */
    public static void addLore(ItemStack item, List<Component> components) {
        final ItemMeta meta = item.getItemMeta();
        meta.lore(components);
        item.setItemMeta(meta);
    }

}
