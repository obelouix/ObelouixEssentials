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
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class AdminToolsGUI extends BaseGUI {

    private @NotNull Inventory adminToolsInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_tools")));
        final ItemMeta itemMeta = null;

        final ItemStack netherite_helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
        final ItemStack netherite_chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        final ItemStack netherite_leggings = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        final ItemStack netherite_boots = new ItemStack(Material.NETHERITE_BOOTS, 1);

        // Protection X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantment(netherite_chestplate, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantment(netherite_leggings, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        addEnchantment(netherite_boots, itemMeta, Enchantment.PROTECTION_ENVIRONMENTAL, 10);

        // Blast Protection X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantment(netherite_chestplate, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantment(netherite_leggings, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);
        addEnchantment(netherite_boots, itemMeta, Enchantment.PROTECTION_EXPLOSIONS, 10);

        // Fire Protection X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantment(netherite_chestplate, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantment(netherite_leggings, itemMeta, Enchantment.PROTECTION_FIRE, 10);
        addEnchantment(netherite_boots, itemMeta, Enchantment.PROTECTION_FIRE, 10);

        // Projectile Protection X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantment(netherite_chestplate, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantment(netherite_leggings, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);
        addEnchantment(netherite_boots, itemMeta, Enchantment.PROTECTION_PROJECTILE, 10);

        // Thorns X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.THORNS, 10);
        addEnchantment(netherite_chestplate, itemMeta, Enchantment.THORNS, 10);
        addEnchantment(netherite_leggings, itemMeta, Enchantment.THORNS, 10);
        addEnchantment(netherite_boots, itemMeta, Enchantment.THORNS, 10);

        // Respiration X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.OXYGEN, 10);

        addEnchantment(netherite_boots, itemMeta, Enchantment.PROTECTION_FALL, 10);

        // Aqua Affinity X
        addEnchantment(netherite_helmet, itemMeta, Enchantment.WATER_WORKER, 10);

        inventory.setItem(0, createAdminArmor(netherite_helmet, "head"));
        inventory.setItem(9, createAdminArmor(netherite_chestplate, "chest"));
        inventory.setItem(18, createAdminArmor(netherite_leggings, "legs"));
        inventory.setItem(27, createAdminArmor(netherite_boots, "feet"));

        return inventory;
    }

    @Override
    public void showInventory(@NotNull Player player) {
        player.openInventory(adminToolsInventory(player));
    }

    @Override
    @EventHandler
    public void cancelClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (event.getView().title().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_tools")))) {
            event.setCancelled(true);
        }
    }

    @Override
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (event.getView().title().equals(Component.text(i18n.sendTranslatedMessage(player, "obelouix.gui.admin_tools")))) {
            if (event.getSlot() == 0) {

                if (player.getInventory().getHelmet() != null) {
                    player.getInventory().addItem(player.getInventory().getHelmet());
                }

                player.getInventory().setHelmet(event.getCurrentItem());

            } else if (event.getSlot() == 9) {

                if (player.getInventory().getChestplate() != null) {
                    player.getInventory().addItem(player.getInventory().getChestplate());
                }

                player.getInventory().setChestplate(event.getCurrentItem());

            } else if (event.getSlot() == 18) {

                if (player.getInventory().getLeggings() != null) {
                    player.getInventory().addItem(player.getInventory().getLeggings());
                }

                player.getInventory().setLeggings(event.getCurrentItem());

            } else if (event.getSlot() == 27) {

                if (player.getInventory().getBoots() != null) {
                    player.getInventory().addItem(player.getInventory().getBoots());
                }

                player.getInventory().setBoots(event.getCurrentItem());
            }
        }
    }

    private ItemStack createAdminArmor(ItemStack item, String slot) {

        final NBTItem nbtItem = new NBTItem(item);
        final NBTList<NBTListCompound> attribute = nbtItem.getCompoundList("AttributeModifiers");
        addAttribute(attribute, "generic.armor", 30, slot);
        addAttribute(attribute, "generic.knockback_resistance", 5, slot);
        addAttribute(attribute, "generic.armor_toughness", 20, slot);
        addAttribute(attribute, "generic.movement_speed", 1.2, slot);
        addAttribute(attribute, "generic.max_health", 1024, slot);

        item = nbtItem.getItem();
        return item;
    }

    private void addAttribute(NBTList<NBTListCompound> attributeList, String attribute, double amount, String slot) {
        final NBTListCompound mod = ((NBTCompoundList) attributeList).addCompound();
        mod.setDouble("Amount", amount);
        mod.setString("AttributeName", attribute);
        mod.setString("Name", attribute);
        mod.setInteger("Operation", 0);
        mod.setInteger("UUIDLeast", -18788761);
        mod.setInteger("UUIDMost", 718533190);
        mod.setString("Slot", slot);
    }

    private void addEnchantment(ItemStack item, ItemMeta itemMeta, Enchantment enchantment, int level) {
        itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(enchantment, level, true);
        item.setItemMeta(itemMeta);
    }

}
