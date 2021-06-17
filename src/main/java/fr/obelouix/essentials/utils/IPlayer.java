package fr.obelouix.essentials.utils;

import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IPlayer {

    static boolean isOnline(String playerName, CommandSender sender) {
        if (Bukkit.getPlayer(playerName) == null && !playerName.equals("*")) {
            sender.sendMessage(ChatColor.GOLD + playerName + " "
                    + ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "player_not_online"));
            return false;
        }
        return true;
    }

    /**
     * This method allow to get the client locale of a player
     *
     * @return the locale of the player
     */
    static String getPlayerLocaleString(@NotNull Player player) {
        return player.locale().toString();
    }

}
