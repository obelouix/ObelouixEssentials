package fr.obelouix.essentials.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class HeadSkinFetcher {

    public static ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId())); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
        skullMeta.displayName(Component.text(player.getName()).decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(68, 255, 51)));
        skull.setItemMeta(skullMeta); // Apply the modified meta to the initial created item
        return skull;
    }

    /**
     * @param skullName
     * @param texture   get the texture ID from https://freshcoal.com/
     * @return
     */
    public static ItemStack getOfflineSkull(String skullName, String texture) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.setProperty(new ProfileProperty("textures", texture));
        skullMeta.setPlayerProfile(profile);
        skullMeta.displayName(Component.text(skullName).decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(68, 255, 51)));
        skull.setItemMeta(skullMeta); // Apply the modified meta to the initial created item
        return skull;
    }

}
