package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.gui.AdminGUI;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminCommand extends BukkitCommand implements Listener {

    private final I18n i18n = I18n.getInstance();

    public AdminCommand(String name) {
        super(name);
    }

    // Nice little method to create a gui item with a custom name, and description
    public static ItemStack createGuiItem(final ItemStack material, final Component component, final List<Component> lore) {
        final ItemStack item = new ItemStack(material.getType(), 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.displayName(component);

        // Set the lore of the item
        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player && IPermission.test(player, "obelouix.commands.admin")) {
            new AdminGUI().showInventory(player);
        }
        return true;
    }
}
