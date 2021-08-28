package fr.obelouix.essentials.gui;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class AdminToolsGUI extends BaseGUI {

    private @NotNull Inventory adminToolsInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_tools")));
        ItemMeta itemMeta = null;

        final ItemStack netherite_helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
        final ItemStack netherite_chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        final ItemStack netherite_leggings = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        final ItemStack netherite_boots = new ItemStack(Material.NETHERITE_BOOTS, 1);

        // Protection X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantement(netherite_chestplate, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantement(netherite_leggings, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantement(netherite_boots, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);

        // Blast Protection X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantement(netherite_chestplate, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantement(netherite_leggings, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantement(netherite_boots, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);

        // Fire Protection X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantement(netherite_chestplate, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantement(netherite_leggings, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantement(netherite_boots, itemMeta, Enchantment.PROTECTION_FIRE, 10);

        // Projectile Protection X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantement(netherite_chestplate, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantement(netherite_leggings, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantement(netherite_boots, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);

        // Thorns X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.THORNS, 10);
        addEnchantement(netherite_chestplate, itemMeta, Enchantment.THORNS, 10);
        addEnchantement(netherite_leggings, itemMeta, Enchantment.THORNS, 10);
        addEnchantement(netherite_boots, itemMeta, Enchantment.THORNS, 10);

        // Respiration X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.OXYGEN, 10);

        // Aqua Affinity X
        addEnchantement(netherite_helmet, itemMeta, Enchantment.WATER_WORKER, 10);

        inventory.setItem(0, createAdminArmor(netherite_helmet, "head"));
        inventory.setItem(9, createAdminArmor(netherite_chestplate, "chest"));
        inventory.setItem(18, createAdminArmor(netherite_leggings, "legs"));
        inventory.setItem(27, createAdminArmor(netherite_boots, "feet"));
/*        ItemStack NETHERITE_CHESTPLATE = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        nbti = new NBTItem(NETHERITE_CHESTPLATE);
        attribute = nbti.getCompoundList("AttributeModifiers");
        mod1 = ((NBTCompoundList) attribute).addCompound();
        mod1.setDouble("Amount", 1E6);
        mod1.setString("AttributeName", "generic.knockback_resistance");
        mod1.setString("Name", "generic.knockback_resistance");
        mod1.setInteger("Operation", 0);
        mod1.setInteger("UUIDLeast", -18788761);
        mod1.setInteger("UUIDMost", 718533190);
        mod1.setString("Slot", "head");
        NETHERITE_CHESTPLATE = nbti.getItem();
        itemMeta = NETHERITE_CHESTPLATE.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
        NETHERITE_CHESTPLATE.setItemMeta(itemMeta);
        inventory.setItem(8, NETHERITE_CHESTPLATE);*/

        return inventory;
    }

    @Override
    public void showInventory(@NotNull Player player) {
        player.openInventory(adminToolsInventory(player));
    }

    @Override
    public void cancelClick(InventoryClickEvent event) {

    }

    @Override
    public void onInventoryClickEvent(InventoryClickEvent event) {

    }

    private ItemStack createAdminArmor(ItemStack item, String slot) {

//        ItemStack NETHERITE_HELMET = new ItemStack(Material.NETHERITE_HELMET, 1);
        NBTItem nbtItem = new NBTItem(item);
        NBTList attribute = nbtItem.getCompoundList("AttributeModifiers");
//        NBTListCompound mod = ((NBTCompoundList) attribute).addCompound();
        addAttribute(attribute, "generic.armor", 30, slot);
        addAttribute(attribute, "generic.knockback_resistance", 5, slot);
        addAttribute(attribute, "generic.armor_toughness", 20, slot);
        addAttribute(attribute, "generic.movement_speed", 1.2, slot);
        addAttribute(attribute, "generic.max_health", 1024, slot);

        item = nbtItem.getItem();
        return item;
    }

    private void addAttribute(NBTList attributeList, String attribute, double amount, String slot) {
        NBTListCompound mod = ((NBTCompoundList) attributeList).addCompound();
        mod.setDouble("Amount", amount);
        mod.setString("AttributeName", attribute);
        mod.setString("Name", attribute);
        mod.setInteger("Operation", 0);
        mod.setInteger("UUIDLeast", -18788761);
        mod.setInteger("UUIDMost", 718533190);
        mod.setString("Slot", slot);
    }

    private void addEnchantement(ItemStack item, ItemMeta itemMeta, Enchantment enchantment, int level) {
        itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(enchantment, level, true);
        item.setItemMeta(itemMeta);
    }

}
