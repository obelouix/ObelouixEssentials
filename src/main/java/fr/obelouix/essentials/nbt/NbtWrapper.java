package fr.obelouix.essentials.nbt;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NbtWrapper {

    public static NBTItem nbtItem(ItemStack item) {
        return new NBTItem(item);
    }

}
