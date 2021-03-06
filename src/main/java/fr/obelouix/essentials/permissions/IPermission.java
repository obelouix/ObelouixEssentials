package fr.obelouix.essentials.permissions;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;


public interface IPermission {

    /**
     * This method will test if a {@link Player} has the right to run a command
     *
     * @param player     {@link Player} who send a command
     * @param permission the {@link Permission} to test on the player
     * @return {@code true} by default , {@code false} if the player doesn't have permission.
     */
    static boolean test(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            final Component message = Component.text(I18n.getInstance().getTranslation(player, "no_permission")).color(TextColor.color(183, 0, 0));
            player.sendMessage(message);
            Essentials.getInstance().getLOGGER().info("Refused command to " + player.getName());
            return false;
        }
        return true;
    }

    /**
     * This method will test if a {@link CommandSender} has the right to run a command
     *
     * @param sender     {@link CommandSender} who send a command
     * @param permission the {@link Permission} to test on the sender
     * @return {@code true} by default , {@code false} if the sender doesn't have permission.
     */
    static boolean test(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getTranslation(sender, "no_permission"));
            Essentials.getInstance().getLOGGER().info("Refused command to " + sender.getName());
            return false;
        }
        return true;
    }

    static boolean simpleTest(Player player, String permission) {
        return player.hasPermission(permission);
    }

    static boolean simpleTest(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
}
