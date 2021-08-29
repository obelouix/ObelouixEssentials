package fr.obelouix.essentials.nbt;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NbtWrapper {

    /**
     * set the {@link ItemStack}  that will have custom NBT data
     *
     * @param item the {@link ItemStack} where we apply NBT
     * @return {@link NBTItem}
     */
    public static NBTItem nbtItem(ItemStack item) {
        return new NBTItem(item);
    }

    /**
     * add a NBT Attribute and also set a slot like {@code "mainhand"} where this tag apply <br/>
     * for example the slot {@code "mainhand} tell the game that this tag apply only when a {@link Player} has the item
     * in his main hand
     *
     * @param attributeList {@linkplain NBTList} of {@link NBTListCompound} attributes
     * @param attribute     a {@link String} like {@code generic.armor}
     * @param amount        amount a {@link Double} like {@code 1.5} that represent the amount to apply on the given attribute
     * @param slot          the slot where this tag apply
     * @see <a href="https://minecraft.fandom.com/wiki/Attribute#Attributes">Attribute list</a>
     */
    public static void addAttribute(NBTList<NBTListCompound> attributeList, String attribute, double amount, String slot) {
        final NBTListCompound mod = NBTCompoundList((NBTCompoundList) attributeList, attribute, amount);
        mod.setString("Slot", slot);
    }

    /**
     * add a NBT Attribute without a slot <br>
     * if you want to add a slot see {@link #addAttribute(NBTList attributeList, String attribute, double amount, String slot)}
     *
     * @param attributeList {@linkplain NBTList} of {@link NBTListCompound} attributes
     * @param attribute     a {@link String} like {@code generic.armor}
     * @param amount        amount a {@link Double} like {@code 1.5} that represent the amount to apply on the given attribute
     * @see <a href="https://minecraft.fandom.com/wiki/Attribute#Attributes">Attribute list</a>
     */
    public static void addAttribute(NBTList<NBTListCompound> attributeList, String attribute, double amount) {
        NBTCompoundList((NBTCompoundList) attributeList, attribute, amount);
    }

    /**
     * @param attributeList {@linkplain NBTList} of {@link NBTListCompound} attributes
     * @param attribute     a {@link String} like {@code generic.armor}
     * @param amount        a {@link Double} like {@code 1.5} that represent the amount to apply on the given attribute
     * @return {@link NBTListCompound}
     * @see <a href="https://minecraft.fandom.com/wiki/Attribute#Attributes">Attribute list</a>
     */
    private static NBTListCompound NBTCompoundList(NBTCompoundList attributeList, String attribute, double amount) {
        final NBTListCompound mod = attributeList.addCompound();
        mod.setDouble("Amount", amount);
        mod.setString("AttributeName", attribute);
        mod.setString("Name", attribute);
        mod.setInteger("Operation", 0);
        mod.setInteger("UUIDLeast", -18788761);
        mod.setInteger("UUIDMost", 718533190);
        return mod;
    }

}
