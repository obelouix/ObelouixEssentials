package fr.obelouix.essentials;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem {

    public static void addEnchantment(ItemStack item, Enchantment enchantment, int enchantLevel) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addEnchant(enchantment, enchantLevel, true);
        item.setItemMeta(meta);
    }

    public static void addEnchantments(ItemStack item, List<Enchantment> enchantments, int enchantLevel) {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        for (Enchantment enchantment : enchantments) {
            meta.addEnchant(enchantment, enchantLevel, true);
        }
        item.setItemMeta(meta);
    }

    public static void setItemName(ItemStack item, Component title) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(title.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
    }

    public static void addLore(ItemStack item, List<Component> components) {
        ItemMeta meta = item.getItemMeta();
        meta.lore(components);
        item.setItemMeta(meta);
    }

}
