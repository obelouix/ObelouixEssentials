package fr.obelouix.essentials.i18n;

import fr.obelouix.essentials.data.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n {

    private static I18n instance;

    private I18n() {
    }

    public static I18n getInstance(){
        if(instance == null){
              instance = new I18n();
        }
        return instance;
    }

    /**
     * Method for sending a message based on player's Locale
     *
     * @param commandSender the player that will receive the message - required to send th message in the right language
     * @param message       a {@link String} ID of the message
     * @return the @param message
     */
    public String sendTranslatedMessage(CommandSender commandSender, String message) {
        final PlayerData playerData = new PlayerData();
        ResourceBundle playerMessages;
        if (commandSender instanceof Player) {
            try {
                final String playerLocale = "messages_" + playerData.getPlayerLocale();
                playerMessages = ResourceBundle.getBundle(playerLocale);
            } catch (MissingResourceException e) {
                playerMessages = ResourceBundle.getBundle("messages_en_US");
            }

        } else {
            playerMessages = ResourceBundle.getBundle("messages_en_US");
        }

        return playerMessages.getString(message);
    }
}

