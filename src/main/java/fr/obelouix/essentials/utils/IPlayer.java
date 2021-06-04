package fr.obelouix.essentials.utils;

import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public interface IPlayer {

    static boolean isOnline(String playerName, CommandSender sender) {
        if (Bukkit.getPlayer(playerName) == null) {
            sender.sendMessage(ChatColor.GOLD + playerName + " "
                    + ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "player_not_online"));
            return false;
        }
        return true;
    }

}
