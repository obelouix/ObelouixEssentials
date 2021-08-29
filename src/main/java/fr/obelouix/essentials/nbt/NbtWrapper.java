package fr.obelouix.essentials.nbt;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.inventory.ItemStack;

public class NbtWrapper {

    public static NBTItem nbtItem(ItemStack item) {
        return new NBTItem(item);
    }

    public static void addAttribute(NBTList<NBTListCompound> attributeList, String attribute, double amount, String slot) {
        final NBTListCompound mod = ((NBTCompoundList) attributeList).addCompound();
        mod.setDouble("Amount", amount);
        mod.setString("AttributeName", attribute);
        mod.setString("Name", attribute);
        mod.setInteger("Operation", 0);
        mod.setInteger("UUIDLeast", -18788761);
        mod.setInteger("UUIDMost", 718533190);
        mod.setString("Slot", slot);
    }

}
