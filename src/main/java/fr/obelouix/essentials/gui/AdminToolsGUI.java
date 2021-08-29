package fr.obelouix.essentials.gui;

import com.google.common.collect.ImmutableList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import fr.obelouix.essentials.items.CustomItem;
import fr.obelouix.essentials.nbt.NbtWrapper;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdminToolsGUI extends BaseGUI {

    private ItemStack worldEditSelectionWand;

    private @NotNull Inventory adminToolsInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(i18n.getTranslation(player, "obelouix.gui.admin_tools")));

        final ItemStack netherite_helmet = new ItemStack(Material.NETHERITE_HELMET, 1);
        final ItemStack netherite_chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        final ItemStack netherite_leggings = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        final ItemStack netherite_boots = new ItemStack(Material.NETHERITE_BOOTS, 1);

        final List<Enchantment> protectionEnchantments = ImmutableList.of(Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_ENVIRONMENTAL);

        final Component untradeableItem = Component.text(i18n.getTranslation(player, "obelouix.untradeable_item"),
                TextColor.color(255, 0, 0)).decoration(TextDecoration.ITALIC, false);

        final List<Component> componentList = new ArrayList<>();
        componentList.add(untradeableItem);

        CustomItem.setItemName(netherite_helmet, Component.text("Admin Helmet", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        CustomItem.addLore(netherite_helmet, componentList);

        CustomItem.setItemName(netherite_chestplate, Component.text("Admin chestplate", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        CustomItem.addLore(netherite_chestplate, componentList);

        CustomItem.setItemName(netherite_leggings, Component.text("Admin leggings", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        CustomItem.addLore(netherite_leggings, componentList);

        CustomItem.setItemName(netherite_boots, Component.text("Admin boots", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        CustomItem.addLore(netherite_boots, componentList);

        // Fire Protection X - Projectile Protection X - Blast Protection X - Protection X
        CustomItem.addEnchantments(netherite_helmet, protectionEnchantments, 10);
        CustomItem.addEnchantments(netherite_chestplate, protectionEnchantments, 10);
        CustomItem.addEnchantments(netherite_leggings, protectionEnchantments, 10);
        CustomItem.addEnchantments(netherite_boots, protectionEnchantments, 10);

        // Thorns X
        CustomItem.addEnchantment(netherite_helmet, Enchantment.THORNS, 10);
        CustomItem.addEnchantment(netherite_chestplate, Enchantment.THORNS, 10);
        CustomItem.addEnchantment(netherite_leggings, Enchantment.THORNS, 10);
        CustomItem.addEnchantment(netherite_boots, Enchantment.THORNS, 10);

        // Respiration X
        CustomItem.addEnchantment(netherite_helmet, Enchantment.OXYGEN, 10);

        //Feather Falling X
        CustomItem.addEnchantment(netherite_boots, Enchantment.PROTECTION_FALL, 10);

        // Aqua Affinity X
        CustomItem.addEnchantment(netherite_helmet, Enchantment.WATER_WORKER, 10);

        // Curse of Biding
        CustomItem.addEnchantment(netherite_helmet, Enchantment.BINDING_CURSE, 1);
        CustomItem.addEnchantment(netherite_chestplate, Enchantment.BINDING_CURSE, 1);
        CustomItem.addEnchantment(netherite_leggings, Enchantment.BINDING_CURSE, 1);
        CustomItem.addEnchantment(netherite_boots, Enchantment.BINDING_CURSE, 1);

        inventory.setItem(0, createAdminArmor(netherite_helmet, "head"));
        inventory.setItem(9, createAdminArmor(netherite_chestplate, "chest"));
        inventory.setItem(18, createAdminArmor(netherite_leggings, "legs"));
        inventory.setItem(27, createAdminArmor(netherite_boots, "feet"));

        if (plugin.isFawePresent()) {
            worldEditSelectionWand = new ItemStack(
                    Material.valueOf(
                            StringUtils.substringAfter(
                                    plugin.getFAWEProvider().getWorldEdit().getConfiguration().wandItem.toUpperCase(Locale.ROOT), ":")));
        }

        addBottomNavigationBar(inventory, player);

        inventory.setItem(32, new ItemStack(worldEditSelectionWand));

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
        if (event.getView().title().equals(Component.text(i18n.getTranslation(player, "obelouix.gui.admin_tools")))) {
            event.setCancelled(true);
        }
    }

    @Override
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (event.getView().title().equals(Component.text(i18n.getTranslation(player, "obelouix.gui.admin_tools")))) {
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

            } else if (event.getSlot() == 32 && plugin.isFawePresent() && IPermission.simpleTest(player, "worldedit.wand")) {
                player.getInventory().addItem(Objects.requireNonNull(event.getCurrentItem()));
            }
        }
    }

    @Override
    public void addBottomNavigationBar(@NotNull Inventory inventory, Player player) {
        //home
        inventory.setItem(49, ItemsGUI.createGuiItem(new ItemStack(Material.BARRIER), Component.text(i18n.getTranslation(player, "obelouix.home"))
                .color(TextColor.color(255, 255, 255)).decoration(TextDecoration.ITALIC, false), null));
    }

    private ItemStack createAdminArmor(ItemStack item, String slot) {

        final NBTItem nbtItem = NbtWrapper.nbtItem(item);
        final NBTList<NBTListCompound> attribute = nbtItem.getCompoundList("AttributeModifiers");
        NbtWrapper.addAttribute(attribute, "generic.armor", 30, slot);
        NbtWrapper.addAttribute(attribute, "generic.knockback_resistance", 5, slot);
        NbtWrapper.addAttribute(attribute, "generic.armor_toughness", 20, slot);
        NbtWrapper.addAttribute(attribute, "generic.movement_speed", 1.2, slot);
        NbtWrapper.addAttribute(attribute, "generic.max_health", 1024, slot);

        item = nbtItem.getItem();
        return item;
    }


}
