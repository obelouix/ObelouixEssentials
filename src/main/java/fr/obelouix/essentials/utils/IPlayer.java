package fr.obelouix.essentials.utils;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IPlayer {

    Essentials plugin = Essentials.getInstance();
    I18n i18n = I18n.getInstance();

    /**
     * Check if the given player is online, if not
     * the sender will see a message in his locale
     *
     * @param playerName name of the player to check
     * @param sender     the sender that requested the check
     * @return {@code true} if the player is online else return {@code false}
     */
    static boolean isOnline(String playerName, CommandSender sender) {
        if (Bukkit.getPlayer(playerName) == null && !playerName.equals("*")) {
            Component message = Component.text(i18n.getTranslation(sender, "player_not_online"))
                    .color(TextColors.DARK_RED.getTextColor())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(Component.text(playerName)
                                    .color(TextColors.GOLD.getTextColor()))
                            .build());

            sender.sendMessage(message);
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

    static String getGroup(Player player) {
        final User user = plugin.getLuckPermsAPI().getPlayerAdapter(Player.class).getUser(player);
        return user.getPrimaryGroup();
    }

}
